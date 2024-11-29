package org.example.app.pages

import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.deps.DomainComponent
import com.omasyo.gatherspace.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map


val tokenStorage = object : TokenStorage {
    var tokenResponse: TokenResponse? = null
    override fun observeToken(): Flow<TokenResponse?>  = flow {
        emit(tokenResponse)
    }

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
val networkComponent = NetworkComponent(tokenStorage)

val domainComponent = DomainComponent(networkComponent, tokenStorage, Dispatchers.Main)

val roomsFlow = domainComponent.roomRepository.getAllRooms().map {
    when(it) {
        AuthError -> emptyList()
        is DomainError -> emptyList()
        is Success -> it.data
    }
}
