package com.omasyo.gatherspace.routes.api.room

import com.omasyo.gatherspace.data.room.RoomRepository
import com.omasyo.gatherspace.models.ErrorResponse
import com.omasyo.gatherspace.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
private data class CreateRoomRequest(val name: String)

@Serializable
private data class AddMembersRequest(val members: List<Int>)

fun Application.roomRoute(repository: RoomRepository) {
    routing {
        get<Rooms.Id> { room ->
            repository.getRoom(room.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respondError(
                ErrorResponse(
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "Room Not Found",
                )
            )
        }

        get<Members> { members ->
            repository.getMembers(members.room.id).let {
                call.respond(HttpStatusCode.OK, it)
            }
        }

        authenticate("auth") {
            post<Rooms> { _ ->
                val room = call.receive<CreateRoomRequest>()
                repository.create(room.name)
                call.respond(HttpStatusCode.Created)
            }

            post<Members> { members ->
                val request = call.receive<AddMembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.Created)
            }

            delete<Members> { members ->
                val request = call.receive<AddMembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}