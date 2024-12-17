package com.omasyo.gatherspace

import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.flow.Flow

interface TokenStorage {
    fun observeToken(): Flow<TokenResponse?>

    suspend fun getToken(): TokenResponse?

    suspend fun saveToken(tokenResponse: TokenResponse)

    suspend fun clearToken()
}