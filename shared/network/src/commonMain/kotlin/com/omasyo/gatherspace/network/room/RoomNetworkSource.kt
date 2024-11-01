package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.network.NetworkSource

interface RoomNetworkSource : NetworkSource {
    suspend fun createRoom(name: String): Result<Unit>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun getRoom(roomId: Int): Result<Room>
}