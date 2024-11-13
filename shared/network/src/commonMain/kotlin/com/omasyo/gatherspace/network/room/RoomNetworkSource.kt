package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.response.CreateRoomResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.NetworkSource

interface RoomNetworkSource : NetworkSource {
    suspend fun createRoom(name: String, description: String): Result<CreateRoomResponse>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun getRoom(roomId: Int): Result<RoomDetails>

    suspend fun getRooms(): Result<List<Room>>
}