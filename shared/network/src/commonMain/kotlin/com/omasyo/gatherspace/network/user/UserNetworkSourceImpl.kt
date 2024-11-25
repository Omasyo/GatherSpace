package com.omasyo.gatherspace.network.user

import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.routes.Users
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import kotlinx.io.Buffer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class UserNetworkSourceImpl(
    private val client: HttpClient
) : UserNetworkSource {
    override suspend fun createAccount(userName: String, password: String, image: Buffer?): Result<Unit> =
        mapResponse {
            client.post(Users()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("", Json.encodeToString(CreateUserRequest(userName, password)))
                            if (image != null) {
                                append("", image)
                            }
                        }
                    )
                )
            }
        }

    override suspend fun deleteAccount(): Result<Unit> =
        mapResponse {
            client.delete(Users())
        }

    override suspend fun getCurrentUser(): Result<UserDetails> =
        mapResponse {
            client.get(Users.Me(Users()))
        }

    override suspend fun getUserById(id: Int): Result<UserDetails> =
        mapResponse {
            client.get(Users.Id(id, Users()))
        }
}