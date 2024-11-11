package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

fun RoomRepository(
    networkSource: RoomNetworkSource,
    dispatcher: CoroutineDispatcher
): RoomRepository = RoomRepositoryImpl(networkSource, dispatcher)

interface RoomRepository {
    fun createRoom(name: String, description: String): Flow<DomainResponse<Unit>>

    fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>>

    fun getRoom(roomId: Int): Flow<DomainResponse<RoomDetails>>

    fun getRooms(): Flow<DomainResponse<List<Room>>>
}