package com.omasyo.gatherspace.data

import com.omasyo.gatherspace.database.Refresh_tokenQueries
import kotlinx.datetime.toKotlinLocalDateTime

class TokenRepositoryImpl(
    private val db: Refresh_tokenQueries
) : TokenRepository {
    override fun createToken(token: String, userId: Int, deviceName: String): Int {
        return db.create(token, userId, deviceName).executeAsOne()
    }

    override fun deleteToken(userId: Int, deviceId: Int) {
        return db.delete(userId, deviceId)
    }

    override fun getWithRefreshToken(token: String): TokenDetails? {
        return db.transactionWithResult {
            db.updateLastAccessed(token)
            db.getByRefreshToken(token) { user_id, device_id, device_name, created, last_accessed ->
                TokenDetails(
                    user_id,
                    device_id,
                    device_name,
                    created.toKotlinLocalDateTime(),
                    last_accessed.toKotlinLocalDateTime()
                )
            }.executeAsOneOrNull()
        }
    }
}