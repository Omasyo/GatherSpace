package com.omasyo.gatherspace.models.response

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: Int,
    val deviceId: Int,
    val deviceName: String,
    val created: LocalDateTime,
    val lastAccessed: LocalDateTime,
)
