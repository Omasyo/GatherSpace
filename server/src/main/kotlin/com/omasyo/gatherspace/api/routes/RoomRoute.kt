package com.omasyo.gatherspace.api.routes

import com.omasyo.gatherspace.data.room.RoomRepository
import com.omasyo.gatherspace.models.request.MembersRequest
import com.omasyo.gatherspace.models.request.CreateRoomRequest
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.models.routes.Members
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.api.auth.AuthName
import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.response.CreateRoomResponse
import com.omasyo.gatherspace.models.routes.User
import com.omasyo.gatherspace.utils.respond
import com.omasyo.gatherspace.utils.toErrorResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.resources.post
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.utils.io.*
import kotlinx.io.Buffer
import kotlinx.serialization.json.Json
import java.io.File

fun Application.roomRoute(repository: RoomRepository) {
    routing {
        get<Rooms> {
            repository.getAllRooms().let {
                call.respond(HttpStatusCode.OK, it)
            }
        }

        get<Rooms.Id> { room ->
            repository.getRoom(room.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(
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

        authenticate(AuthName) {

            get<User.Rooms> {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

                repository.getUserRooms(userId).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }

            post<Rooms> { _ ->
                val multiPartData = call.receiveMultipart()
                var details: CreateRoomRequest? = null
                var imageBuffer: Buffer? = null
                var error: ErrorResponse? = null

                multiPartData.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            details = Json.decodeFromString(part.value)
                        }

                        is PartData.FileItem -> {
                            if (part.contentType?.contentType != "image") {
                                error = ErrorResponse(
                                    statusCode = HttpStatusCode.BadRequest.value,
                                    message = "Invalid image type"
                                )

                            }
                            imageBuffer = part.provider().readBuffer()
                        }

                        else -> Unit
                    }
                }
                error?.let { return@post call.respond(it) }

                val user = details ?: return@post call.respond(
                    ErrorResponse(
                        statusCode = HttpStatusCode.BadRequest.value,
                        message = "Room details not set"
                    )
                )

                when (val result = repository.create(details!!.name, user.description, imageBuffer)) {
                    is DatabaseResponse.Failure -> call.respond(result.toErrorResponse())
                    is DatabaseResponse.Success -> call.respond(
                        HttpStatusCode.Created,
                        CreateRoomResponse(result.data)
                    )
                }
            }

            post<Members> { members ->
                val request = call.receive<MembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.Created)
            }

            delete<Members> { members ->
                val request = call.receive<MembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}