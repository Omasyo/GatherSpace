package com.omasyo.gatherspace.domain.room

import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.io.Buffer

internal class RoomRepositoryImpl(
    private val networkSource: RoomNetworkSource,
    private val dispatcher: CoroutineDispatcher
) : RoomRepository {
    override fun createRoom(name: String, description: String, image: Buffer?): Flow<DomainResponse<Int>> =
        flow {
            if (name.isBlank()) {
                emit(DomainError("Room name cannot be blank"))
                return@flow
            }
            emit(networkSource.createRoom(name.trim(), description.trim(), image).mapToDomain { it.id })
        }.flowOn(dispatcher)

    override fun addMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.addMembers(roomId, memberIds).mapToDomain())
        }.flowOn(dispatcher)

    override fun removeMembers(roomId: Int, memberIds: List<Int>): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.removeMembers(roomId, memberIds).mapToDomain())
        }.flowOn(dispatcher)

    override fun getRoom(roomId: Int): Flow<DomainResponse<RoomDetails>> =
        flow {
            emit(networkSource.getRoom(roomId).mapToDomain())
        }.flowOn(dispatcher)

    override fun getUserRooms(): Flow<DomainResponse<List<Room>>> =
        flow {
            emit(networkSource.getUserRooms().mapToDomain())
        }.flowOn(dispatcher)

    override fun getAllRooms(): Flow<DomainResponse<List<Room>>> =
        flow {
            emit(networkSource.getAllRooms().mapToDomain())
        }.flowOn(dispatcher)
}