package com.omasyo.gatherspace.network.auth

import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.routes.Session
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.plugins.auth.authProviders

fun AuthNetworkSource(client: HttpClient): AuthNetworkSource = AuthNetworkSourceImpl(client)

internal class AuthNetworkSourceImpl(
    private val client: HttpClient
) : AuthNetworkSource {
    override suspend fun login(
        username: String,
        password: String,
        deviceName: String
    ): Result<TokenResponse> =
        mapResponse {
            client.post(Session()) {
                setBody(LoginRequest(username, password, deviceName))
            }.also {
                clearAuth()
            }
        }

    override suspend fun logout(deviceId: Int?): Result<Unit> = mapResponse {
        client.delete(Session(deviceId))
    }


    private fun clearAuth() { //TODO places neeed you
        client.authProviders.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()?.clearToken()
    }
}