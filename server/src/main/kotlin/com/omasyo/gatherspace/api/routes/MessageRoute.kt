package com.omasyo.gatherspace.api.routes

import com.omasyo.gatherspace.data.message.MessageRepository
import com.omasyo.gatherspace.models.request.MessageRequest
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.routes.Messages
import com.omasyo.gatherspace.api.auth.AuthName
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.server.sse.*
import kotlinx.coroutines.flow.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val messageSharedFlow = MutableSharedFlow<Message>()

fun Application.messageRoute(repository: MessageRepository) {
    routing {
        authenticate(AuthName) {
            post<Messages> { path ->

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

                val messageRequest = call.receive<MessageRequest>()
                val message = repository.create(messageRequest.content, userId, path.room.id)
                messageSharedFlow.emit(message)

                call.respond(HttpStatusCode.Created)
            }

        }

        get<Messages> { path ->
            repository.getMessages(path.room.id, path.before, path.limit).let { messages ->
                call.respond(messages)
            }
        }

        sse("/rooms/{roomId}/messages/events") {
            val roomId = call.parameters["roomId"]?.toInt()!!

            messageSharedFlow.filter { it.roomId == roomId }.collect { message ->
                send(Json.encodeToString<Message>(message))
            }
        }
    }
}