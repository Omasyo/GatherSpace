package com.omasyo.gatherspace.models.response

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
)