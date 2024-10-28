package com.omasyo.gatherspace

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.JWT
import com.omasyo.gatherspace.routes.api.auth.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.Date
import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(val username: String, val password: String, val deviceName: String)

@Serializable
data class RefreshTokenRequest(val refreshToken: String)


fun Application.configureAuth(authService: AuthService) {
    install(Authentication) {
        jwt("auth") {
            verifier(authService.accessTokenVerifier)
            validate { credential ->
                if (credential.payload.getClaim("token_type").asString() == TokenType.ACCESS_TOKEN.name) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm -> //TODO come up with an actual error type
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired $realm")
            }
        }
        jwt("refresh-token") {
            verifier(authService.refreshTokenVerifier)
            validate { credential ->
                if (credential.payload.getClaim("token_type").asString() == TokenType.REFRESH_TOKEN.name) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { defaultScheme, realm -> //TODO come up with an actual error type
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired $realm")
            }
        }
    }
    routing {
        post("/session") {
            val userCredentials = call.receive<UserCredentials>()

            if (!authService.validateUser(userCredentials.username, userCredentials.password)) {
                call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
            }
            val user = authService.getUserByUsername(userCredentials.username)!!

            val tokenResponse = authService.generateTokens(user.id, userCredentials.deviceName)

            call.respond(tokenResponse)
        }

        put("/session") {
            val refreshTokenRequest = call.receive<RefreshTokenRequest>()

            val tokenResponse = authService.refreshTokens(refreshTokenRequest.refreshToken)

            tokenResponse?.let { call.respond(it) } ?: call.respond(HttpStatusCode.Unauthorized, "Token not found")

        }

        authenticate("auth") {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()

                val username = principal!!.payload.getClaim("user_id").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}

enum class TokenType {
    ACCESS_TOKEN, REFRESH_TOKEN
}