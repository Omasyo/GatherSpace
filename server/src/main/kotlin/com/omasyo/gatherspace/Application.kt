package com.omasyo.gatherspace

import com.omasyo.gatherspace.data.createMessageRepository
import com.omasyo.gatherspace.data.createRoomRepository
import com.omasyo.gatherspace.data.createUserRepository
import com.omasyo.gatherspace.data.database
import com.omasyo.gatherspace.routes.api.message.messageController
import com.omasyo.gatherspace.routes.api.room.roomController
import com.omasyo.gatherspace.routes.api.user.userController
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
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
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    routing {
        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }
    }
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
    userController(createUserRepository(database))
    roomController(createRoomRepository(database))
    messageController(createMessageRepository(database))
}