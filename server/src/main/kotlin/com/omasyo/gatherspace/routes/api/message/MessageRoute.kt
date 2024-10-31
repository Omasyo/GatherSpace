package com.omasyo.gatherspace.routes.api.message

import com.omasyo.gatherspace.data.message.MessageRepository
import com.omasyo.gatherspace.models.Message
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val messageSharedFlow = MutableSharedFlow<Message>()

@Serializable
private data class MessageRequest(val content: String)

fun Application.messageRoute(repository: MessageRepository) {
    routing {
        authenticate("auth") {
            post<Routes> { path ->

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

                val messageRequest = call.receive<MessageRequest>()
                val message = repository.create(messageRequest.content, userId, path.room.id)
                messageSharedFlow.emit(message)

                call.respond(HttpStatusCode.Created)
            }

        }

        sse("/rooms/{roomId}/messages") {
            val roomId = call.parameters["roomId"]?.toInt()!!

            messageSharedFlow.filter { it.roomId == roomId }.collect { message ->
                send(Json.encodeToString<Message>(message))
            }
        }
    }
}