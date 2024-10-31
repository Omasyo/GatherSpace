package com.omasyo.gatherspace.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val content: String,
    val senderId: Int?,
    val roomId: Int,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)