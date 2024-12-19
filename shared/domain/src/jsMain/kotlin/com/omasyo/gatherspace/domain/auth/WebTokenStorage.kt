package com.omasyo.gatherspace.domain.auth

import com.omasyo.gatherspace.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import kotlinx.browser.localStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class WebTokenStorage : TokenStorage {
    private val tokenKey = "token"

    private val insertTokenEvent = MutableStateFlow(Any())

    override fun observeToken(): Flow<TokenResponse?> = insertTokenEvent.map {
        localStorage.getItem(tokenKey)?.let { JSON.parse(it) }
    }

    override suspend fun getToken(): TokenResponse? =
        localStorage.getItem(tokenKey)?.let { JSON.parse(it) }


    override suspend fun saveToken(tokenResponse: TokenResponse) {
        insertTokenEvent.emit(Any())
        localStorage.setItem(tokenKey, JSON.stringify(tokenResponse))
    }

    override suspend fun clearToken() {
        localStorage.removeItem(tokenKey)
    }

}