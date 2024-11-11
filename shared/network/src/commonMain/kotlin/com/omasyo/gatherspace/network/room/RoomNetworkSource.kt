package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.NetworkSource
import io.ktor.client.*

fun RoomNetworkSource(client: HttpClient): RoomNetworkSource = RoomNetworkSourceImpl(client)

interface RoomNetworkSource : NetworkSource {
    suspend fun createRoom(name: String, description: String): Result<Unit>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun getRoom(roomId: Int): Result<RoomDetails>

    suspend fun getRooms(): Result<List<Room>>
}