package com.omasyo.gatherspace.network

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.request.RefreshTokenRequest
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.routes.Session
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*

expect fun provideEngine(): HttpClientEngine

fun createClient(
    engine: HttpClientEngine = provideEngine(),
    tokenStorage: TokenStorage
) =
    HttpClient(engine) {
        install(SSE)
        install(Resources)
        install(ContentNegotiation) {
            json()
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val tokenResponse = tokenStorage.getToken()
                    Napier.i(tag = "createClient") { "Loaded tokens $tokenResponse" }
                    tokenResponse?.let {
                        BearerTokens(it.accessToken, it.refreshToken)
                    }
                }
                refreshTokens {
                    val response = client.patch(Session()) {
                        markAsRefreshTokenRequest()
                        setBody(RefreshTokenRequest(oldTokens?.refreshToken ?: ""))
                    }
                    Napier.i(tag = "createClient") { "refreshToken response $response" }
                    if (response.status.isSuccess()) {
                        val tokenResponse: TokenResponse = response.body()
                        tokenStorage.saveToken(tokenResponse)
                        BearerTokens(tokenResponse.accessToken, tokenResponse.refreshToken)
                    } else {
                        null
                    }
                }
            }
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTP
                host = "192.168.79.134"
                port = 8080
            }
            contentType(ContentType.Application.Json)
        }

    }