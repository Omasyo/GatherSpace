package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.request.CreateRoomRequest
import com.omasyo.gatherspace.models.request.MembersRequest
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.routes.Members
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.request.*

internal class RoomNetworkSourceImpl(
    private val client: HttpClient
) : RoomNetworkSource {
    override suspend fun createRoom(name: String): Result<Unit> = mapResponse {
        client.post(Rooms()) {
            setBody(CreateRoomRequest(name))
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

    override suspend fun getRooms(): Result<List<Room>> =
        mapResponse {
            client.get(Rooms())
        }
}