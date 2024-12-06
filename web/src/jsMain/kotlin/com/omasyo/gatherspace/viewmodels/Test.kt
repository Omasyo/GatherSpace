package com.omasyo.gatherspace.viewmodels

import com.omasyo.gatherspace.TokenStorage
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.browser.localStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map


val tokenStorage = object : TokenStorage {
    val tokenKey = "token"

    private val insertTokenEvent = MutableStateFlow(Any())

    override fun observeToken(): Flow<TokenResponse?> = insertTokenEvent.map {
        localStorage.getItem(tokenKey)?.let { JSON.parse(it) }
    }

    override suspend fun getToken(): TokenResponse? {
        return localStorage.getItem(tokenKey)?.let { JSON.parse(it) }
    }

    override suspend fun saveToken(tokenResponse: TokenResponse) {
        insertTokenEvent.emit(Any())
        localStorage.setItem(tokenKey, JSON.stringify(tokenResponse))
    }

    override suspend fun clearToken() {
        localStorage.removeItem(tokenKey)
    }

}
val networkComponent = NetworkComponent(tokenStorage)

val domainComponent = DomainComponent(networkComponent, tokenStorage, Dispatchers.Main)
