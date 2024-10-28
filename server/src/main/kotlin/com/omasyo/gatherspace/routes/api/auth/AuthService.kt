package com.omasyo.gatherspace.routes.api.auth

import com.auth0.jwt.JWTVerifier
import com.omasyo.gatherspace.models.TokenResponse
import com.omasyo.gatherspace.models.User
import com.omasyo.gatherspace.models.UserDetails

const val TOKEN_VALID_DURATION = 60000

interface AuthService {
    val accessTokenVerifier: JWTVerifier

    val refreshTokenVerifier: JWTVerifier

    fun generateTokens(userId: Int, deviceName: String): TokenResponse

    fun refreshTokens(refreshToken: String): TokenResponse?

    fun revokeToken(token: String)

    fun validateUser(username: String, password: String): Boolean

    fun getUserByUsername(username: String): UserDetails?
}