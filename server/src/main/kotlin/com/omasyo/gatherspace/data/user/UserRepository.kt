package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.models.response.UserDetails

fun createUserRepository(database: Database): UserRepository =
    UserRepositoryImpl(database.user_accountQueries)

interface UserRepository {
    fun create(username: String, password: String, image: String?)

    fun getUserById(id: Int): UserDetails?

    fun getUserByUsername(username: String): UserDetails?

    fun validateUser(username: String, password: String): Boolean
}

