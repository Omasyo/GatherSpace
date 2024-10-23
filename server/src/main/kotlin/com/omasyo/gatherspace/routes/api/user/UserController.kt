package com.omasyo.gatherspace.routes.api.user

import com.omasyo.gatherspace.data.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(val name: String, val password: String)

fun Application.userController(repository: UserRepository) {
    routing {
        get<Users.Id> { user ->
            println("Looking for ${user.id}")
            repository.getUserById(user.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        post<Users> { _ ->
            val user = call.receive<UserRequest>()
            repository.create(user.name, user.password)
            call.respond(HttpStatusCode.Created)
        }
    }
}