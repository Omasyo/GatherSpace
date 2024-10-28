package com.omasyo.gatherspace

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.omasyo.gatherspace.data.*
import com.omasyo.gatherspace.routes.api.auth.AuthServiceImpl
import com.omasyo.gatherspace.routes.api.message.messageController
import com.omasyo.gatherspace.routes.api.room.roomController
import com.omasyo.gatherspace.routes.api.user.userController
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    val userRepository = createUserRepository(database)
    configureAuth(AuthServiceImpl(userRepository, TokenRepositoryImpl(database.refresh_tokenQueries)))
    userController(userRepository)
    roomController(createRoomRepository(database))
    messageController(createMessageRepository(database))
}