package com.omasyo.gatherspace.domain

import com.omasyo.gatherspace.network.NetworkException
import kotlinx.coroutines.*

import kotlinx.coroutines.flow.*
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.random.Random

fun <T> Flow<T>.toCallback(
    coroutineScope: CoroutineScope,
    callback: (T) -> Unit
) {
    coroutineScope.launch {
        collect { callback(it) }
    }
}

expect fun getDeviceName(): String

fun <T> Result<T>.mapToDomain() = fold(
    onSuccess = { Success(it) },
    onFailure = { error ->
        if (error is NetworkException) {
            if (error.error.statusCode == 401) {
                AuthError
            } else {
                DomainError(error.error.message)
            }
        } else {
            DomainError("An Error Occurred")
        }
    }
)