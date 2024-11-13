package com.omasyo.gatherspace.network.auth

import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.network.NetworkSource

interface AuthNetworkSource : NetworkSource {
    suspend fun login(
        username: String,
        password: String,
        deviceName: String
    ): Result<TokenResponse>

    suspend fun logout(deviceId: Int? = null): Result<Unit>
}