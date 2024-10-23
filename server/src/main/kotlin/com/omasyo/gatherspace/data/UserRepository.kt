package com.omasyo.gatherspace.data

import com.omasyo.gatherspace.models.UserDetails
import kotlinx.datetime.toKotlinLocalDateTime

fun createUserRepository(database: Database) = UserRepositoryImpl(database.user_accountQueries)

interface UserRepository {
    fun create(name: String, passwordHash: String)

    fun getUserById(id: Int): UserDetails?
}


class UserRepositoryImpl(
    private val db: User_accountQueries
) : UserRepository {
    override fun create(name: String, passwordHash: String) {
        db.create(name, passwordHash)
    }

    override fun getUserById(id: Int): UserDetails? {
        return db.get(id) { userId, username, created, modified ->
            UserDetails(
                userId,
                username,
                created.toKotlinLocalDateTime(),
                modified.toKotlinLocalDateTime()
            )
        }.executeAsOneOrNull()
    }
}