package com.omasyo.gatherspace.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val username: String,
)