package com.omasyo.gatherspace.network.auth

import com.omasyo.gatherspace.models.request.LoginRequest
import com.omasyo.gatherspace.network.NetworkSource

interface AuthNetworkSource : NetworkSource {
    suspend fun login(
        username: String,
        password: String,
        deviceName: String
    ): Result<Unit>

    suspend fun logout(deviceId: Int? = null): Result<Unit>
}