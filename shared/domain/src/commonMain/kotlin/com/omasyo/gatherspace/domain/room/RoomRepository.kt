package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.io.Buffer

interface RoomRepository {
    fun createRoom(name: String, description: String, image: Buffer?): Flow<DomainResponse<Int>>

    fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    fun joinRoom(roomId: Int): Flow<DomainResponse<Unit>>

    fun leaveRoom(roomId: Int): Flow<DomainResponse<Unit>>

    fun getRoom(roomId: Int): Flow<DomainResponse<RoomDetails>>

    fun getUserRooms(): Flow<DomainResponse<List<Room>>>

    fun getAllRooms(): Flow<DomainResponse<List<Room>>>
}