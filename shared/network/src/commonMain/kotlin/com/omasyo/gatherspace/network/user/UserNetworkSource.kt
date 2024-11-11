package com.omasyo.gatherspace.network.user

import com.omasyo.gatherspace.network.NetworkSource
import io.ktor.client.*

fun UserNetworkSource(client: HttpClient): UserNetworkSource =
    UserNetworkSourceImpl(client)

interface UserNetworkSource : NetworkSource {
    suspend fun createAccount(userName: String, password: String): Result<Unit>

    suspend fun deleteAccount(): Result<Unit>
}