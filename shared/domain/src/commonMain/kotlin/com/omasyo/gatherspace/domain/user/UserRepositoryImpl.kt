package com.omasyo.gatherspace.domain.user

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.network.user.UserNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class UserRepositoryImpl(
    private val userNetworkSource: UserNetworkSource,
    private val dispatcher: CoroutineDispatcher
) : UserRepository {
    override suspend fun createAccount(userName: String, password: String): Flow<DomainResponse<Unit>> =
        flow {
            emit(userNetworkSource.createAccount(userName, password).mapToDomain())
        }.flowOn(dispatcher)

    override suspend fun deleteAccount(): Flow<DomainResponse<Unit>> =
        flow {
            emit(userNetworkSource.deleteAccount().mapToDomain())
        }.flowOn(dispatcher)
}