package com.omasyo.gatherspace

import com.omasyo.gatherspace.data.createRoomRepository
import com.omasyo.gatherspace.data.createUserRepository
import com.omasyo.gatherspace.data.database
import com.omasyo.gatherspace.routes.api.room.roomController
import com.omasyo.gatherspace.routes.api.user.userController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
    userController(createUserRepository(database))
    roomController(createRoomRepository(database))
}