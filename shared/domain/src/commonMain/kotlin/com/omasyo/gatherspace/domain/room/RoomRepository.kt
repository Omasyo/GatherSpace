package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Room
import kotlinx.coroutines.flow.Flow

interface RoomRepository {
    suspend fun createRoom(name: String): Flow<DomainResponse<Unit>>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    suspend fun getRoom(roomId: Int): Flow<DomainResponse<Room>>
}