package com.omasyo.gatherspace.api.routes

import com.omasyo.gatherspace.api.auth.AuthName
import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.data.user.UserRepository
import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.response.ErrorResponse
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
import io.ktor.server.response.*
import io.ktor.server.routing.routing
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.io.Buffer
import kotlinx.io.asByteChannel
import kotlinx.io.transferFrom
import kotlinx.serialization.json.Json
import java.io.File

fun Application.userRoute(repository: UserRepository) {
    routing {
        get<Users.Id> { user ->
            repository.getUserById(user.id)?.let {
                call.respond(HttpStatusCode.OK, it)
            } ?: call.respond(
                ErrorResponse(
                    statusCode = HttpStatusCode.NotFound.value,
                    message = "User Not Found",
                )
            )
        }

        authenticate(AuthName) {
            get<Users.Me> {
                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!

                repository.getUserById(userId)?.let {
                    call.respond(HttpStatusCode.OK, it)
                } ?: call.respond(
                    ErrorResponse(
                        statusCode = HttpStatusCode.NotFound.value,
                        message = "User Not Found",
                    )
                )
            }

            get<Users.Me.Sessions> {
                val principal = call.principal<JWTPrincipal>()

                val userId = principal?.payload?.getClaim("user_id")?.asInt()!!
                repository.getUserSessions(userId).let {
                    call.respond(HttpStatusCode.OK, it)
                }
            }
        }

        post<Users> { _ ->
            val multiPartData = call.receiveMultipart()
            var details: CreateUserRequest? = null
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
                    message = "User details not set"
                )
            )

            when (val result = repository.create(details!!.username, user.password, imageBuffer)) {
                is DatabaseResponse.Failure -> call.respond(result.toErrorResponse())
                is DatabaseResponse.Success -> call.respond(HttpStatusCode.Created)
            }
        }
    }
}