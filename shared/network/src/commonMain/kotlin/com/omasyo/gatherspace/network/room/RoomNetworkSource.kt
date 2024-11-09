package com.omasyo.gatherspace.network.room

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.network.NetworkSource
import com.omasyo.gatherspace.network.createClient
import com.omasyo.gatherspace.network.provideEngine
import kotlinx.coroutines.flow.Flow

val client = createClient(
    provideEngine(), object : TokenStorage {
        override val tokenFlow: Flow<TokenResponse?>
            get() = TODO("Not yet implemented")

        override suspend fun getToken(): TokenResponse {
            return TokenResponse(accessToken = "mollis", refreshToken = "regione")
        }

        override suspend fun saveToken(tokenResponse: TokenResponse) {
        }

        override suspend fun clearToken() {
            TODO("Not yet implemented")
        }

    })
fun createNetworkSource(): RoomNetworkSource = RoomNetworkSourceImpl(client)

interface RoomNetworkSource : NetworkSource {
    suspend fun createRoom(name: String): Result<Unit>

    suspend fun addMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun removeMembers(roomId: Int, memberIds: List<Int>): Result<Unit>

    suspend fun getRoom(roomId: Int): Result<RoomDetails>

    suspend fun getRooms(): Result<List<Room>>
}