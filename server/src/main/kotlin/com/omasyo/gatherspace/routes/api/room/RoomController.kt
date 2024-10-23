package com.omasyo.gatherspace.routes.api.room

import com.omasyo.gatherspace.data.RoomRepository
import com.omasyo.gatherspace.data.UserRepository
import com.omasyo.gatherspace.routes.api.user.UserRequest
import com.omasyo.gatherspace.routes.api.user.Users
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

@Serializable
data class RoomRequest(val name: String)

fun Application.roomController(repository: RoomRepository) {
    routing {
        get<Rooms.Id> { room ->
            repository.getRoom(room.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        post<Rooms> { _ ->
            val room = call.receive<RoomRequest>()
            repository.create(room.name)
            call.respond(HttpStatusCode.Created)
        }
    }
}