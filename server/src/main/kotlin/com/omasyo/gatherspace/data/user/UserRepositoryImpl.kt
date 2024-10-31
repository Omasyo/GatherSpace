package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.database.User_accountQueries
import com.omasyo.gatherspace.models.UserDetails
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

class UserRepositoryImpl(
    private val db: User_accountQueries
) : UserRepository {
    override fun create(username: String, password: String) {
        db.create(username, password)
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
        created: LocalDateTime,
        modified: LocalDateTime
    ) = UserDetails(
        id = userId,
        username = username,
        created = created.toKotlinLocalDateTime(),
        modified = modified.toKotlinLocalDateTime()
    )
}