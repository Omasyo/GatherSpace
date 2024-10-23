package com.omasyo.gatherspace.data

import com.omasyo.gatherspace.models.UserAccount

fun createUserRepository(database: Database) = UserRepositoryImpl(database.user_accountQueries)

interface UserRepository {
    fun create(name: String, passwordHash: String)

    fun getUserById(id: Int): UserAccount?
}


class UserRepositoryImpl(
    private val db: User_accountQueries
) : UserRepository {
    override fun create(name: String, passwordHash: String) {
        db.create(name, passwordHash)
    }

    override fun getUserById(id: Int): UserAccount? {
        return db.get(id, UserAccount::fromDb).executeAsOneOrNull()
    }
}