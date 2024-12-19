package com.omasyo.gatherspace.models.response

import kotlinx.serialization.Serializable
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

@OptIn(ExperimentalJsExport::class)
@JsExport
@Serializable
data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
)
