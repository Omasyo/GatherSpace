package com.omasyo.gatherspace.data.token

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.models.TokenDetails

fun createTokenRepository(database: Database): TokenRepository =
    TokenRepositoryImpl(database.refresh_tokenQueries)

interface TokenRepository {
    fun createToken(token: String, userId: Int, deviceName: String): Int

    fun deleteToken(userId: Int, deviceId: Int)

    fun getWithRefreshToken(token: String): TokenDetails?
}