package com.omasyo.gatherspace.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.forms.submitForm
import io.ktor.http.URLProtocol
import io.ktor.http.isSuccess
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun provideEngine(): HttpClientEngine

fun createClient(
    engine: HttpClientEngine
) =
    HttpClient(engine) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "192.168.21.134"
                port = 8080
            }
        }
        install(WebSockets) {
            pingIntervalMillis = 20_000
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }

    }