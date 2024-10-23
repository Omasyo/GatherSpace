package com.omasyo.gatherspace.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed interface DatabaseResponse<out T>

@JvmInline
value class Success<T>(val value: T) : DatabaseResponse<T>

@Serializable
data class ErrorObject(
    val message: String,
    val extra: @Serializable @Contextual Any? = null
) : DatabaseResponse<Nothing>