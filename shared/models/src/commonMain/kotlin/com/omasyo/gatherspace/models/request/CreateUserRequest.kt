package com.omasyo.gatherspace.models.request

import kotlinx.serialization.Serializable


@Serializable
data class CreateUserRequest(
    val name: String,
    val password: String
)
