package com.omasyo.gatherspace.domain.user

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.io.Buffer

interface UserRepository {
    fun createAccount(userName: String, password: String, image: Buffer?): Flow<DomainResponse<Unit>>

    fun deleteAccount(): Flow<DomainResponse<Unit>>

    fun getCurrentUser(): Flow<DomainResponse<UserDetails>>

    fun getUserById(userId: Int): Flow<DomainResponse<UserDetails>>

    fun updateUser(username: String?, password: String?, image: Buffer?): Flow<DomainResponse<Unit>>

    fun getUserSessions(): Flow<DomainResponse<List<UserSession>>>

    fun deleteUserSession(deviceId: Int): Flow<DomainResponse<Unit>>
}