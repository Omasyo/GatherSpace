package com.omasyo.gatherspace.api.auth

import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.models.request.RefreshTokenRequest
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.models.routes.Session
import com.omasyo.gatherspace.utils.respond
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.routing

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
                call.respond(
                    ErrorResponse(
                        statusCode = statusCode.value,
                        message = "Valid token not provided"
                    )
                )
            }
        }
    }
    routing {
        post<Session> {
            val loginRequest = call.receive<LoginRequest>()

            if (!authService.validateUser(loginRequest.username, loginRequest.password)) {
                call.respond(
                    ErrorResponse(
                        statusCode = HttpStatusCode.NotFound.value,
                        message = "Invalid username or password"
                    )
                )
            }
            val user = authService.getUserByUsername(loginRequest.username)!!

            authService.generateTokens(user.id, loginRequest.deviceName).let {
                call.respond(it)
            }
        }

        patch<Session> {
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

        authenticate(AuthName) {
            delete<Session> { session ->
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.payload.getClaim(JwtKeys.USER_ID).asInt()
                val deviceId = session.deviceId ?: principal.payload.getClaim(JwtKeys.DEVICE_ID).asInt()

                authService.revokeToken(userId = userId, deviceId = deviceId)
                call.respond(HttpStatusCode.NoContent)
            }
        }
    }
}