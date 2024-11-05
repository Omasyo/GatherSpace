package com.omasyo.gatherspace.models.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val content: String,
    val senderId: User?,
    val roomId: Int,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)