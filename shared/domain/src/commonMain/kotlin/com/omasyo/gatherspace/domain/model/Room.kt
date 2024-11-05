package com.omasyo.gatherspace.domain.model

import kotlinx.datetime.LocalDateTime
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class Room(
    val id: Int,
    val name: String,
    val members: List<User>,
    val created: String,
    val modified: String,
)
