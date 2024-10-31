package com.omasyo.gatherspace.network

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import kotlinx.serialization.json.Json

expect fun provideEngine(): HttpClientEngine

fun createClient(
    engine: HttpClientEngine
) =
    HttpClient(engine) {
        install(SSE)
//        install(ContentNegotiation) {
//            json()
//        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
                port = 8080
            }
        }
        install(WebSockets) {
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

    }