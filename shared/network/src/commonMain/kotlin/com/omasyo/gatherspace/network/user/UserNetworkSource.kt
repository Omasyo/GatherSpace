package com.omasyo.gatherspace.network.user

import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.network.NetworkSource
import kotlinx.io.Buffer

interface UserNetworkSource : NetworkSource {
    suspend fun createAccount(userName: String, password: String, image: Buffer?): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>

    suspend fun getCurrentUser(): Result<UserDetails>

    suspend fun getUserById(id: Int): Result<UserDetails>
}