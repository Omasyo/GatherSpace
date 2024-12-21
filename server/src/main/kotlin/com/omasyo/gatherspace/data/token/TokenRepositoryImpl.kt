package com.omasyo.gatherspace.data.token

import com.omasyo.gatherspace.models.TokenDetails
import com.omasyo.gatherspace.database.Refresh_tokenQueries
import kotlinx.datetime.toKotlinLocalDateTime

class TokenRepositoryImpl(
    private val db: Refresh_tokenQueries
) : TokenRepository {
    override fun createToken(token: String, userId: Int, deviceName: String): Int =
        db.create(token, userId, deviceName).executeAsOne()


    override fun deleteToken(userId: Int, deviceId: Int) = db.delete(userId, deviceId)


    override fun getWithRefreshToken(token: String): TokenDetails? =
        db.transactionWithResult {
            db.updateLastAccessed(token)
            db.getByRefreshToken(token) { userId, deviceId, deviceName, created, lastAccessed ->
                TokenDetails(
                    userId = userId,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    created = created.toKotlinLocalDateTime(),
                    lastAccessed = lastAccessed.toKotlinLocalDateTime()
                )
            }.executeAsOneOrNull()
        }
}