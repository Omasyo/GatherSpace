package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.models.response.UserDetails

interface UserRepository {
    fun create(username: String, password: String, image: String?)

    fun getUserById(id: Int): UserDetails?

    fun getUserByUsername(username: String): UserDetails?

    fun validateUser(username: String, password: String): Boolean
}

