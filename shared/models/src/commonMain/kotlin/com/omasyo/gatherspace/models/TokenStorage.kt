package com.omasyo.gatherspace.models

import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.flow.Flow


interface TokenStorage {
    suspend fun getToken(): TokenResponse?

    suspend fun saveToken(tokenResponse: TokenResponse)

    suspend fun clearToken()
}