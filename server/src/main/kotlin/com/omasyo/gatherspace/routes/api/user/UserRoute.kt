package com.omasyo.gatherspace.routes.api.user

import com.omasyo.gatherspace.data.user.UserRepository
import com.omasyo.gatherspace.models.ErrorResponse
import com.omasyo.gatherspace.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@Serializable
private data class UserRequest(
    val name: String,
    val password: String
)

fun Application.userController(repository: UserRepository) {
    routing {
        get<Users.Id> { user ->
            repository.getUserById(user.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondError(
                ErrorResponse(
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "User Not Found",
                )
            )
        }

        post<Users> { _ ->
            val user = call.receive<UserRequest>()
            repository.create(user.name, user.password)
            call.respond(HttpStatusCode.Created)
        }
    }
}