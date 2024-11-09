package com.omasyo.gatherspace.api.routes

import com.omasyo.gatherspace.data.user.UserRepository
import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.models.routes.Users
import com.omasyo.gatherspace.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing

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
            val user = call.receive<CreateUserRequest>()
            repository.create(user.name, user.password, null)
            call.respond(HttpStatusCode.Created)
        }
    }
}