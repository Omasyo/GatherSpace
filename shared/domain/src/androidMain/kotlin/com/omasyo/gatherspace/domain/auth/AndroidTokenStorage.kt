package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse

class AndroidTokenStorage : TokenStorage {
    private var tokenResponse: TokenResponse? = null

    override suspend fun getToken(): TokenResponse? {
        return tokenResponse
    }

    override suspend fun saveToken(tokenResponse: TokenResponse) {
        this.tokenResponse = tokenResponse
    }

    override suspend fun clearToken() {
        tokenResponse = null
    }
}