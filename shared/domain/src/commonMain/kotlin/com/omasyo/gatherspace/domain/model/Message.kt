package com.omasyo.gatherspace.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Message(
    val id: Int,
    val content: String,
    val senderId: User?,
    val roomId: Int,
    val created: String,
    val modified: String,
)