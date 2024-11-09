package com.omasyo.gatherspace.models.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserDetails(
    val id: Int,
    val username: String,
    val imageUrl: String?,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)
