package com.omasyo.gatherspace.models.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    val id: Int,
    val name: String,
    val imageUrl: String?
)

@Serializable
data class RoomDetails(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val isMember: Boolean,
    val members: List<User>,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)
