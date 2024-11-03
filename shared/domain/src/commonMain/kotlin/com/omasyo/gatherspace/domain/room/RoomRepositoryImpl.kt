package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomRepositoryImpl(
    private val networkSource: RoomNetworkSource
) : RoomRepository {
    override suspend fun createRoom(name: String): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.createRoom(name).mapToDomain())
        }

    override suspend fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.addMembers(roomId, memberIds).mapToDomain())
        }

    override suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.removeMembers(roomId, memberIds).mapToDomain())
        }

    override suspend fun getRoom(roomId: Int): Flow<DomainResponse<Room>> =
        flow {
            emit(networkSource.getRoom(roomId).mapToDomain())
        }
}