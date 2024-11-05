package com.omasyo.gatherspace.domain.model

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
data class User(
    val id: Int,
    val username: String,
)