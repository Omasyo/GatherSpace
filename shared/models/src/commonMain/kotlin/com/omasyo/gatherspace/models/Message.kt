package com.omasyo.gatherspace.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@Serializable
data class Message(
    val id: Int,
    val content: String,
    val senderId: Int?,
    val roomId: Int,
    val created: LocalDateTime,
    val modified: LocalDateTime,
)


@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class MessageRequest(val senderId: Int?, val content: String) //TODO: whether to put this