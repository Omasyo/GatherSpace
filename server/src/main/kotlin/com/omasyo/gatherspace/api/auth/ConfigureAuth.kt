package com.omasyo.gatherspace.api.auth

import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.models.request.RefreshTokenRequest
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.utils.respondError
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val AuthName = "auth"

fun Application.configureAuth(authService: AuthService) {
    install(Authentication) {
        jwt(AuthName) {
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
            val loginRequest = call.receive<LoginRequest>()

            if (!authService.validateUser(loginRequest.username, loginRequest.password)) {
                call.respond(
                    ErrorResponse(
                        statusCode = HttpStatusCode.Unauthorized.value,
                        message = "Invalid username or password"
                    )
                )
            }
            val user = authService.getUserByUsername(loginRequest.username)!!

            authService.generateTokens(user.id, loginRequest.deviceName).let {
                call.respond(it)
            }
        }

        patch("/session") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()

            val response = authService.refreshTokens(refreshTokenRequest.refreshToken)

            response?.let {
                call.respond(it)
            } ?: call.respondError(
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