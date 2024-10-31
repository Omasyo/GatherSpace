package com.omasyo.gatherspace.models.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val statusCode: Int, val message: String)