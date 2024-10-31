package com.omasyo.gatherspace.routes.api.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.omasyo.gatherspace.data.token.TokenRepository
import com.omasyo.gatherspace.data.user.UserRepository
import com.omasyo.gatherspace.models.*
import java.time.Instant
import java.util.*

class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository
) :
    AuthService {
    override val accessTokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withClaim(JwtKeys.TOKEN_TYPE, TokenType.ACCESS_TOKEN.name)
        .build()

    override val refreshTokenVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(refreshSecret))
        .withClaim(JwtKeys.TOKEN_TYPE, TokenType.REFRESH_TOKEN.name)
        .build()

    override fun generateTokens(userId: Int, deviceName: String): TokenResponse {
        val refreshToken = JWT.create()
            .withClaim(JwtKeys.TOKEN_TYPE, TokenType.REFRESH_TOKEN.name)
            .withIssuedAt(Instant.now())
            .sign(Algorithm.HMAC256(refreshSecret))

        val deviceId = tokenRepository.createToken(refreshToken, userId, deviceName)

        val accessToken = generateAccessToken(userId, deviceId)

        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun refreshTokens(refreshToken: String): TokenResponse? {
        val tokenDetails = tokenRepository.getWithRefreshToken(refreshToken) ?: return null

        val accessToken = generateAccessToken(tokenDetails.userId, tokenDetails.deviceId)
        return TokenResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    override fun revokeToken(userId: Int, deviceId: Int) {
        tokenRepository.deleteToken(userId, deviceId)
    }

    override fun validateUser(username: String, password: String): Boolean {
        return userRepository.validateUser(username, password)
    }

    override fun getUserByUsername(username: String): UserDetails? {
        return userRepository.getUserByUsername(username)
    }

    private fun generateAccessToken(userId: Int, deviceId: Int): String {
        return JWT.create()
            .withClaim("user_id", userId)
            .withClaim("token_type", TokenType.ACCESS_TOKEN.name)
            .withClaim("device_id", deviceId)
            .withExpiresAt(Date(System.currentTimeMillis() + TOKEN_VALID_DURATION))
            .sign(Algorithm.HMAC256(secret))
    }
}