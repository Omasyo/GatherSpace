package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.response.CreateRoomResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.NetworkSource
import kotlinx.io.Buffer

interface RoomNetworkSource : NetworkSource {
    suspend fun createRoom(name: String, description: String, image: Buffer?): Result<CreateRoomResponse>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun joinRoom(roomId: Int): Result<Unit>

    suspend fun leaveRoom(roomId: Int): Result<Unit>

    suspend fun getRoom(roomId: Int): Result<RoomDetails>

    suspend fun getUserRooms(): Result<List<Room>>

    suspend fun getAllRooms(): Result<List<Room>>
}