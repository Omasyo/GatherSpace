package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomRepositoryImpl(
    private val networkSource: RoomNetworkSource
) : RoomRepository {
    override fun createRoom(name: String): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.createRoom(name).mapToDomain())
        }

    override fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.addMembers(roomId, memberIds).mapToDomain())
        }

    override fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.removeMembers(roomId, memberIds).mapToDomain())
        }

    override fun getRoom(roomId: Int): Flow<DomainResponse<RoomDetails>> =
        flow {
            emit(networkSource.getRoom(roomId).mapToDomain())
        }

    override fun getRooms(): Flow<DomainResponse<List<Room>>> =
        flow {
            emit(networkSource.getRooms().mapToDomain())
        }
}