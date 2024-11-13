package com.omasyo.gatherspace.network.auth

import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.routes.Session
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*

internal class AuthNetworkSourceImpl(
    private val client: HttpClient
) : AuthNetworkSource {
    override suspend fun login(
        username: String,
        password: String,
        deviceName: String
    ): Result<TokenResponse> =
        mapResponse<TokenResponse> {
            client.post(Session()) {
                setBody(LoginRequest(username, password, deviceName))
            }
        }.onSuccess { clearAuth() }

    override suspend fun logout(deviceId: Int?): Result<Unit> = mapResponse<Unit> {
        client.delete(Session(deviceId))
    }.onSuccess { clearAuth() }


    private fun clearAuth() {
        client.authProviders.filterIsInstance<BearerAuthProvider>()
            .firstOrNull()?.clearToken()
    }
}