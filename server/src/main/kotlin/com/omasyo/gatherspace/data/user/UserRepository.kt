package com.omasyo.gatherspace.data.user

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.models.response.UserDetails
import kotlinx.io.Buffer

interface UserRepository {
    fun create(username: String, password: String, imageBuffer: Buffer?): DatabaseResponse<Unit>

    fun getUserById(id: Int): UserDetails?

    fun getUserByUsername(username: String): UserDetails?

    fun validateUser(username: String, password: String): Boolean
}

