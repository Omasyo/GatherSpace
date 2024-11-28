package com.omasyo.gatherspace.network.user

import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.request.UpdateUserRequest
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import com.omasyo.gatherspace.models.routes.Session
import com.omasyo.gatherspace.models.routes.Users
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.io.Buffer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class UserNetworkSourceImpl(
    private val client: HttpClient
) : UserNetworkSource {
    override suspend fun createAccount(username: String, password: String, image: Buffer?): Result<Unit> =
        mapResponse {
            client.post(Users()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("", Json.encodeToString(CreateUserRequest(username, password)))
                            if (image != null) {
                                append("", image, Headers.build {
                                    append(HttpHeaders.ContentType, "image/*")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"\""
                                    )
                                })
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
            client.get(Users.Me())
        }

    override suspend fun getUserById(id: Int): Result<UserDetails> =
        mapResponse {
            client.get(Users.Id(id, Users()))
        }

    override suspend fun updateUser(username: String?, password: String?, image: Buffer?): Result<Unit> =
        mapResponse {
            client.patch(Users.Me()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("", Json.encodeToString(UpdateUserRequest(username, password)))
                            if (image != null) {
                                append("", image, Headers.build {
                                    append(HttpHeaders.ContentType, "image/*")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=\"\""
                                    )
                                })
                            }
                        }
                    )
                )
            }
        }

    override suspend fun getUserSessions(): Result<List<UserSession>> =
        mapResponse {
            client.get(Users.Me.Sessions())
        }

    override suspend fun deleteUserSession(deviceId: Int): Result<Unit> =
        mapResponse {
            client.delete(Session(deviceId))
        }
}