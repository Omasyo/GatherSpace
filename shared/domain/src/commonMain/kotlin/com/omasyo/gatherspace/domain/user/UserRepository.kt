package com.omasyo.gatherspace.domain.user

import com.omasyo.gatherspace.domain.DomainResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun createAccount(userName: String, password: String): Flow<DomainResponse<Unit>>

    suspend fun deleteAccount(): Flow<DomainResponse<Unit>>
}