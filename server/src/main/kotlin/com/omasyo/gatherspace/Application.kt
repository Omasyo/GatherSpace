package com.omasyo.gatherspace

import com.omasyo.gatherspace.data.*
import com.omasyo.gatherspace.data.message.createMessageRepository
import com.omasyo.gatherspace.data.room.RoomRepositoryImpl
import com.omasyo.gatherspace.data.token.TokenRepositoryImpl
import com.omasyo.gatherspace.data.user.UserRepositoryImpl
import com.omasyo.gatherspace.routes.api.auth.AuthServiceImpl
import com.omasyo.gatherspace.routes.api.auth.configureAuth
import com.omasyo.gatherspace.routes.api.message.messageRoute
import com.omasyo.gatherspace.routes.api.room.roomRoute
import com.omasyo.gatherspace.routes.api.user.userController
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.resources.*
import io.ktor.server.sse.*
import io.ktor.server.websocket.*
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    val database = createDatabase("postgres", "pass")
    val userRepository = UserRepositoryImpl(database.user_accountQueries)

    install(Resources)
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.CacheControl)
        allowHeader(HttpHeaders.ContentType)
        anyMethod()
        anyHost()
    }
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(SSE)
    configureAuth(AuthServiceImpl(userRepository, TokenRepositoryImpl(database.refresh_tokenQueries)))
    userController(userRepository)
    roomRoute(RoomRepositoryImpl(database.roomQueries, database.room_memberQueries))
    messageRoute(createMessageRepository(database))
}