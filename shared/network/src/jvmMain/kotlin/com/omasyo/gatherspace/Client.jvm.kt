package com.omasyo.gatherspace

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.routes.Messages
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.network.createClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
    val client = createClient(CIO.create(), object : TokenStorage {
        var tokenResponse = TokenResponse(
            "1eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX2lkIjoxLCJ0b2tlbl90eXBlIjoiQUNDRVNTX1RPS0VOIiwiZGV2aWNlX2lkIjoyLCJleHAiOjE3MzAzNzU5MTh9.L9HazXwW4d56UEvnAQwUBYgVVTOQMNR_VJMvsHcmtOs",
            "2eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiUkVGUkVTSF9UT0tFTiIsImlhdCI6MTczMDM2OTkxN30.mFiw83HyK6KtONByxEQtLvC_xrkP5DkfRGq_2buM3hI"
        )
        override val tokenFlow: Flow<TokenResponse?>
            get() = TODO("Not yet implemented")

        override suspend fun getToken(): TokenResponse {
            return tokenResponse
        }

        override suspend fun saveToken(accessTokenResponse: TokenResponse) {
            tokenResponse = accessTokenResponse
        }

        override suspend fun clearToken() {
            TODO("Not yet implemented")
        }

    })
    runBlocking {
        client.get(Messages(Rooms.Id(1))).let {
            println(it.bodyAsText())
            println(it.status)
        }
    }
}