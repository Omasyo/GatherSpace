package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.database.Refresh_tokenQueries
import com.omasyo.gatherspace.database.User_accountQueries
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import com.omasyo.gatherspace.utils.createImageFile
import io.ktor.http.*
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.io.Buffer
import org.postgresql.util.PSQLException
import java.time.LocalDateTime

class UserRepositoryImpl(
    private val userAccountQueries: User_accountQueries,
    private val refreshTokenQueries: Refresh_tokenQueries
) : UserRepository {
    override fun create(username: String, password: String, imageBuffer: Buffer?): DatabaseResponse<Unit> {
        return try {
            userAccountQueries.transactionWithResult {
                val imageId = imageBuffer?.let { createImageFile(it) }
                DatabaseResponse.Success(userAccountQueries.create(username, password, imageId))
            }
        } catch (e: PSQLException) {
            when (e.sqlState) {
                "23505" -> DatabaseResponse.Failure("This user already exists", HttpStatusCode.Conflict.value)
                else -> DatabaseResponse.Failure("An Error Occurred", HttpStatusCode.InternalServerError.value)
            }
        }
    }

    override fun getUserById(id: Int): UserDetails? {
        return userAccountQueries.getById(id, ::userMapper).executeAsOneOrNull()
    }

    override fun getUserByUsername(username: String): UserDetails? {
        return userAccountQueries.getByUsername(username, ::userMapper).executeAsOneOrNull()
    }

    override fun validateUser(username: String, password: String): Boolean {
        return userAccountQueries.validateUser(
            username = username,
            password = password
        ).executeAsOneOrNull() ?: false
    }

    override fun getUserSessions(userId: Int): List<UserSession> {
        return refreshTokenQueries.getByUserId(userId) { _, deviceId, deviceName, created, lastAccessed ->
            UserSession(
                userId = userId,
                deviceId = deviceId,
                deviceName = deviceName,
                created = created.toKotlinLocalDateTime(),
                lastAccessed = lastAccessed.toKotlinLocalDateTime()
            )
        }.executeAsList()
    }
}

fun userMapper(
    userId: Int,
    username: String,
    image: String?,
    created: LocalDateTime,
    modified: LocalDateTime
) = UserDetails(
    id = userId,
    username = username,
    imageUrl = image,
    created = created.toKotlinLocalDateTime(),
    modified = modified.toKotlinLocalDateTime()
)