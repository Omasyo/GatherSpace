package com.omasyo.gatherspace.data

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.database.User_accountQueries
import com.omasyo.gatherspace.models.UserDetails
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

fun createUserRepository(database: Database) = UserRepositoryImpl(database.user_accountQueries)

interface UserRepository {
    fun create(username: String, password: String)

    fun getUserById(id: Int): UserDetails?

    fun getUserByUsername(username: String): UserDetails?

    fun validateUser(username: String, password: String): Boolean
}


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
        userId,
        username,
        created.toKotlinLocalDateTime(),
        modified.toKotlinLocalDateTime()
    )
}