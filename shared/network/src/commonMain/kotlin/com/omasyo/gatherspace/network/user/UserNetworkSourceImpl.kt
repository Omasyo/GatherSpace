package com.omasyo.gatherspace.network.user

import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.routes.Users
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*

internal class UserNetworkSourceImpl(
    private val client: HttpClient
) : UserNetworkSource {
    override suspend fun createAccount(userName: String, password: String): Result<Unit> =
        mapResponse {
            client.post(Users()) {
                setBody(CreateUserRequest(userName, password))
            }
        }

    override suspend fun deleteAccount(): Result<Unit> =
        mapResponse {
            client.delete(Users())
        }
}