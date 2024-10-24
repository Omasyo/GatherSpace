package com.omasyo.gatherspace.routes.api.message

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.data.ErrorObject
import com.omasyo.gatherspace.data.MessageRepository
import com.omasyo.gatherspace.data.Success
import com.omasyo.gatherspace.models.MessageRequest
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.time.LocalDateTime



val messageResponseFlow = MutableSharedFlow<MessageRequest>()
val sharedFlow = messageResponseFlow.asSharedFlow()

var count = 0

fun Application.messageController(repository: MessageRepository) {
    routing {
        get<Messages> { messages ->
            repository.getMessages(messages.room.id, LocalDateTime.now(), 10).let {
                call.respond(HttpStatusCode.OK, it)
            }
        }

        post<Messages> { path ->
            val message = call.receive<MessageRequest>()
            val response = repository.create(message.content, message.senderId, path.room.id)

            call.respond(response)
        }

        webSocket("/rooms/{roomId}") {
            val roomId = call.parameters["roomId"]?.toInt() ?: return@webSocket

//            send("You are connected to WebSocket! with ${++count} others")

            val job = launch {
                sharedFlow.collect { message ->
                    repository.create(message.content, message.senderId, roomId)
                    sendSerialized(message)
                }
            }

            runCatching {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val receivedMessage = converter!!.deserialize<MessageRequest>(frame)
                        messageResponseFlow.emit(receivedMessage)
                    }
                }
            }.onFailure { exception ->
                println("WebSocket exception: ${exception.localizedMessage}")
            }.also {
                --count
                job.cancel()
            }
        }
    }
}

suspend fun <T> RoutingCall.respond(response: DatabaseResponse<T>) {
    when (response) {
        is ErrorObject -> respond(HttpStatusCode.BadRequest, response)
        is Success -> respond(HttpStatusCode.Created)
    }
}