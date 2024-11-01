package com.omasyo.gatherspace.network.auth

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.models.routes.Session
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.plugins.resources.*
import io.ktor.client.*
import io.ktor.client.request.*

class AuthNetworkSourceImpl(
    private val client: HttpClient,
    private val tokenStorage: TokenStorage
) : AuthNetworkSource {
    override suspend fun login(
        username: String,
        password: String,
        deviceName: String
    ): Result<Unit> =
        mapResponse {
            client.post(Session()) {
                setBody(LoginRequest(username, password, deviceName))
            }
        }

    override suspend fun logout(deviceId: Int?): Result<Unit> = mapResponse {
        client.delete(Session(deviceId))
    }
}