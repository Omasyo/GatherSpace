package com.omasyo.gatherspace.api.auth

import com.auth0.jwt.JWTVerifier
import com.omasyo.gatherspace.models.response.TokenResponse
import com.omasyo.gatherspace.models.response.UserDetails

const val TOKEN_VALID_DURATION = 6000000

const val ACCESS_TOKEN_SECRET = "hidden"
const val REFRESH_TOKEN_SECRET = "hidden-too"

object JwtKeys {
    const val TOKEN_TYPE = "token_type"
    const val USER_ID = "user_id"
    const val DEVICE_ID = "device_id"
}

enum class TokenType {
    ACCESS_TOKEN, REFRESH_TOKEN
}


interface AuthService {
    val accessTokenVerifier: JWTVerifier

    val refreshTokenVerifier: JWTVerifier

    fun generateTokens(userId: Int, deviceName: String): TokenResponse

    fun refreshTokens(refreshToken: String): TokenResponse?

    fun revokeToken(userId: Int, deviceId: Int)

    fun validateUser(username: String, password: String): Boolean

    fun getUserByUsername(username: String): UserDetails?
}