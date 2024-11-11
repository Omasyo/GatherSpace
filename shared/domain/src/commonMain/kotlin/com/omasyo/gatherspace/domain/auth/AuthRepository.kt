package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.network.auth.AuthNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

fun AuthRepository(
    authNetworkSource: AuthNetworkSource,
    tokenStorage: TokenStorage,
    dispatcher: CoroutineDispatcher
): AuthRepository = AuthRepositoryImpl(authNetworkSource, tokenStorage, dispatcher)

interface AuthRepository {
    fun login(username: String, password: String): Flow<DomainResponse<Unit>>

    fun logout(): Flow<DomainResponse<Unit>>
}

