package com.omasyo.gatherspace.routes.api.auth

import com.omasyo.gatherspace.models.ErrorResponse
import com.omasyo.gatherspace.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
private data class UserCredentials(
    val username: String,
    val password: String,
    val deviceName: String
)

@Serializable
private data class RefreshTokenRequest(val refreshToken: String)

fun Application.configureAuth(authService: AuthService) {
    install(Authentication) {
        jwt("auth") {
            verifier(authService.accessTokenVerifier)
            validate { credential ->
                JWTPrincipal(credential.payload)

            }
            challenge { _, _ ->
                val statusCode = HttpStatusCode.Unauthorized
                call.respondError(
                    ErrorResponse(
                        statusCode = statusCode.value,
                        message = "Valid token not provided"
                    )
                )
            }
        }
    }
    routing {
        post("/session") {
            val userCredentials = call.receive<UserCredentials>()

            if (!authService.validateUser(userCredentials.username, userCredentials.password)) {
                call.respond(
                    ErrorResponse(
                        statusCode = HttpStatusCode.Unauthorized.value,
                        message = "Invalid username or password"
                    )
                )
            }
            val user = authService.getUserByUsername(userCredentials.username)!!

            authService.generateTokens(user.id, userCredentials.deviceName).let {
                call.respond(it)
            }
        }

        patch("/session") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()

            val response = authService.refreshTokens(refreshTokenRequest.refreshToken)

            response?.let {
                call.respond(it)
            } ?: call.respond(
                ErrorResponse(
                    statusCode = HttpStatusCode.Unauthorized.value,
                    message = "Token not found"
                )
            )

        }

        delete("/session") {
            val principal = call.principal<JWTPrincipal>()!!
            val userId = principal.payload.getClaim(JwtKeys.USER_ID).asInt()
            val deviceId = principal.payload.getClaim(JwtKeys.DEVICE_ID).asInt()

            authService.revokeToken(userId = userId, deviceId = deviceId)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}