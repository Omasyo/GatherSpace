package com.omasyo.gatherspace.domain.user

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.UserDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.io.Buffer

interface UserRepository {
    suspend fun createAccount(userName: String, password: String, image: Buffer?): Flow<DomainResponse<Unit>>

    suspend fun deleteAccount(): Flow<DomainResponse<Unit>>

    suspend fun getCurrentUser(): Flow<DomainResponse<UserDetails>>

    suspend fun getUserById(userId: Int): Flow<DomainResponse<UserDetails>>
}