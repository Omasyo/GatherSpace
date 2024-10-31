package com.omasyo.gatherspace.models

import kotlinx.datetime.LocalDateTime

data class TokenDetails(
    val userId: Int,
    val deviceId: Int,
    val deviceName: String,
    val created: LocalDateTime,
    val lastAccessed: LocalDateTime,
)
