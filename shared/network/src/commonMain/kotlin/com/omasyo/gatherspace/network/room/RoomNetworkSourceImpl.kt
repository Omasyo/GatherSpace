package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.request.CreateRoomRequest
import com.omasyo.gatherspace.models.request.CreateUserRequest
import com.omasyo.gatherspace.models.request.MembersRequest
import com.omasyo.gatherspace.models.response.CreateRoomResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.routes.Members
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.models.routes.User
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.io.Buffer
import kotlinx.io.readByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal class RoomNetworkSourceImpl(
    private val client: HttpClient
) : RoomNetworkSource {
    override suspend fun createRoom(name: String, description: String, image: Buffer?): Result<CreateRoomResponse> =
        mapResponse {
            client.post(Rooms()) {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append("", Json.encodeToString(CreateRoomRequest(name, description)))
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

    override suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit> =
        mapResponse {
            client.post(Members(Rooms.Id(roomId))) {
                setBody(MembersRequest(memberIds))
            }
        }

    override suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit> =
        mapResponse {
            client.delete(Members(Rooms.Id(roomId))) {
                setBody(MembersRequest(memberIds))
            }
        }

    override suspend fun getRoom(roomId: Int): Result<RoomDetails> =
        mapResponse {
            client.get(Rooms.Id(roomId))
        }

    override suspend fun getUserRooms(): Result<List<Room>> =
        mapResponse {
            client.get(User.Rooms(User()))
        }

    override suspend fun getAllRooms(): Result<List<Room>> =
        mapResponse {
            client.get(Rooms())
        }
}