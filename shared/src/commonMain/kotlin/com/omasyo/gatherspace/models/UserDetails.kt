package com.omasyo.gatherspace.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val id: Int,
    val username: String,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)
