package com.omasyo.gatherspace

import com.omasyo.gatherspace.api.auth.AuthService
import com.omasyo.gatherspace.api.auth.AuthServiceImpl
import com.omasyo.gatherspace.api.auth.configureAuth
import com.omasyo.gatherspace.api.routes.imageRoute
import com.omasyo.gatherspace.api.routes.messageRoute
import com.omasyo.gatherspace.api.routes.roomRoute
import com.omasyo.gatherspace.api.routes.userRoute
import com.omasyo.gatherspace.data.createDatabase
import com.omasyo.gatherspace.data.message.MessageRepository
import com.omasyo.gatherspace.data.message.MessageRepositoryImpl
import com.omasyo.gatherspace.data.room.RoomRepository
import com.omasyo.gatherspace.data.room.RoomRepositoryImpl
import com.omasyo.gatherspace.data.token.TokenRepository
import com.omasyo.gatherspace.data.token.TokenRepositoryImpl
import com.omasyo.gatherspace.data.user.UserRepository
import com.omasyo.gatherspace.data.user.UserRepositoryImpl
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
    configureAuth(authService)
    userRoute(userRepository)
    roomRoute(roomRepository)
    messageRoute(messageRepository)
    imageRoute()
}


val database = createDatabase("postgres", "pass")
val userRepository: UserRepository = UserRepositoryImpl(database.user_accountQueries, database.refresh_tokenQueries)
val tokenRepository: TokenRepository = TokenRepositoryImpl(database.refresh_tokenQueries)
val roomRepository: RoomRepository =
    RoomRepositoryImpl(database.roomQueries, database.room_memberQueries, database.user_accountQueries)
val messageRepository: MessageRepository = MessageRepositoryImpl(database.messageQueries)
val authService: AuthService = AuthServiceImpl(userRepository, tokenRepository)