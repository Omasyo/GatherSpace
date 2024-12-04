package com.omasyo.gatherspace.api.routes

import com.auth0.jwt.JWT
import com.omasyo.gatherspace.api.auth.AuthName
import com.omasyo.gatherspace.api.auth.JwtKeys
import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.data.room.RoomRepository
import com.omasyo.gatherspace.models.request.CreateRoomRequest
import com.omasyo.gatherspace.models.request.MembersRequest
import com.omasyo.gatherspace.models.request.UserRoomRequest
import com.omasyo.gatherspace.models.response.CreateRoomResponse
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.models.routes.Users
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

fun Application.roomRoute(repository: RoomRepository) {
    routing {
        get<Rooms> {
            repository.getAllRooms().let {
                call.respond(HttpStatusCode.OK, it)
            }
        }

        get<Rooms.Id> { room ->

//            val principal = call.principal<JWTPrincipal>()
            //todo find a better way - scopes?
            val token = call.request.headers["Authorization"]?.substring(7)
            val userId = token?.let { JWT.decode(it)?.getClaim(JwtKeys.USER_ID)?.asInt() }

//            val userId = principal?.payload?.getClaim("user_id")!!.asInt()

            repository.getRoom(room.id, userId)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(
                ErrorResponse(
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "Room Not Found",
                )
            )
        }

        get<Rooms.Members> { members ->
            repository.getMembers(members.room.id).let {
                call.respond(HttpStatusCode.OK, it)
            }
        }

        authenticate(AuthName) {

            get<Users.Me.Rooms> {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

                repository.getUserRooms(userId).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }

            post<Users.Me.Rooms> {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!
                val roomId = call.receive<UserRoomRequest>().roomId

                repository.addMembers(roomId, listOf(userId)).let {
                    call.respond(HttpStatusCode.Created, it)
                }
            }

            delete<Users.Me.Rooms> {

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!
                val roomId = call.receive<UserRoomRequest>().roomId

                repository.removeMembers(roomId, listOf(userId)).let {
                    call.respond(HttpStatusCode.NoContent, it)
                }
            }

            post<Rooms> { _ ->

                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

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

                when (val result = repository.create(details!!.name, user.description, userId, imageBuffer)) {
                    is DatabaseResponse.Failure -> call.respond(result.toErrorResponse())
                    is DatabaseResponse.Success -> call.respond(
                        HttpStatusCode.Created,
                        CreateRoomResponse(result.data)
                    )
                }
            }

            post<Rooms.Members> { members ->
                //TODO check if user add himself or owner of room adding a user(probably not)

                val request = call.receive<MembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.Created)
            }

            delete<Rooms.Members> { members ->
                //TODO check if user remove himself or owner of room remove users
                val request = call.receive<MembersRequest>()
                repository.addMembers(members.room.id, request.members)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}