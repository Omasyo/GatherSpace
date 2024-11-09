package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.database.User_accountQueries
import com.omasyo.gatherspace.models.response.UserDetails
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class UserRepositoryImpl(
    private val db: User_accountQueries
) : UserRepository {
    override fun create(username: String, password: String, image: String?) {
        db.create(username, password, image)
    }

    override fun getUserById(id: Int): UserDetails? {
        return db.getById(id, ::userMapper).executeAsOneOrNull()
    }

    override fun getUserByUsername(username: String): UserDetails? {
        return db.getByUsername(username, ::userMapper).executeAsOneOrNull()
    }

    override fun validateUser(username: String, password: String): Boolean {
        return db.validateUser(
            username = username,
            password = password
        ).executeAsOneOrNull() ?: false
    }

    private fun userMapper(
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
}