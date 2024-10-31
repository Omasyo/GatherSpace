package com.omasyo.gatherspace.network

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.request.RefreshTokenRequest
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.routes.Session
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

expect fun provideEngine(): HttpClientEngine

fun createClient(
    engine: HttpClientEngine,
    tokenStorage: TokenStorage
) =
    HttpClient(engine) {
        install(SSE)
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
//        install(WebSockets) {
//            pingIntervalMillis = 20_000
//            contentConverter = KotlinxWebsocketSerializationConverter(Json)
//        }
        install(Auth) {
            bearer {
                loadTokens {
                    val tokenResponse = tokenStorage.getToken()
                    BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                }
                refreshTokens {
                    val response = client.patch(Session()) {
                        markAsRefreshTokenRequest()
                        setBody(RefreshTokenRequest(oldTokens?.refreshToken ?: ""))
                    }
                    if (response.status.isSuccess()) {
                        val tokenResponse: TokenResponse = response.body()
                        tokenStorage.saveToken(tokenResponse)
                        BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                    } else {
                        null
                    }
                }
//                sendWithoutRequest { request ->
//                    request.url.host == "api.spotify.com"
//                    true
//                }
            }
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "localhost"
                port = 8080
            }
            contentType(ContentType.Application.Json)
        }

    }