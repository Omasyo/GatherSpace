package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.getDeviceName
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.network.auth.AuthNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class AuthRepositoryImpl(
    private val authNetworkSource: AuthNetworkSource,
    private val tokenStorage: TokenStorage,
    private val dispatcher: CoroutineDispatcher
) : AuthRepository {
    override fun isAuthenticated(): Flow<Boolean> =
        tokenStorage.observeToken().map {
            it != null
        }


    override fun login(username: String, password: String): Flow<DomainResponse<Unit>> =
        flow {
            emit(authNetworkSource.login(username, password, getDeviceName())
                .onSuccess { tokenStorage.saveToken(it) }
                .map { }.mapToDomain()
            )
        }.flowOn(dispatcher)

    override fun logout(): Flow<DomainResponse<Unit>> = flow {
        tokenStorage.clearToken()
        emit(authNetworkSource.logout().mapToDomain())
    }.flowOn(dispatcher)
}