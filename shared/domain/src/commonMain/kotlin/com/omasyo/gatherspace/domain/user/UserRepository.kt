package com.omasyo.gatherspace.domain.user

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.network.user.UserNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

fun UserRepository(
    networkSource: UserNetworkSource,
    dispatcher: CoroutineDispatcher
): UserRepository = UserRepositoryImpl(networkSource, dispatcher)

interface UserRepository {
    suspend fun createAccount(userName: String, password: String): Flow<DomainResponse<Unit>>

    suspend fun deleteAccount(): Flow<DomainResponse<Unit>>
}