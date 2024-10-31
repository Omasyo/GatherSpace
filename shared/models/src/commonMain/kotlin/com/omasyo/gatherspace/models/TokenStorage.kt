package com.omasyo.gatherspace.models

import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.coroutines.flow.Flow


interface TokenStorage {
    val tokenFlow: Flow<TokenResponse?>

    suspend fun getToken(): TokenResponse

    suspend fun saveToken(accessTokenResponse: TokenResponse)

    suspend fun clearToken()
}