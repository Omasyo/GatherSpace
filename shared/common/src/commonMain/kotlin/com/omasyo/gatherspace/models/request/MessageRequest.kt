package com.omasyo.gatherspace.models.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(val content: String)

