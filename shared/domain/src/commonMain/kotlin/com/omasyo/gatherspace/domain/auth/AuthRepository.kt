package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.domain.DomainResponse
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(username: String, password: String): Flow<DomainResponse<Unit>>

    fun logout(): Flow<DomainResponse<Unit>>
}

