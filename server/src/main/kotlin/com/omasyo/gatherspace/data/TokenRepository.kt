package com.omasyo.gatherspace.data

import kotlinx.datetime.LocalDateTime


data class TokenDetails(
    val userId: Int,
    val deviceId: Int,
    val deviceName: String,
    val created: LocalDateTime,
    val lastAccessed: LocalDateTime,
)

interface TokenRepository {
    fun createToken(token: String, userId: Int, deviceName: String): Int

    fun deleteToken(userId: Int, deviceId: Int)

    fun getWithRefreshToken(token: String): TokenDetails?
}