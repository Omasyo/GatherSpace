@file:OptIn(ExperimentalJsExport::class)
@file:JsExport

package com.omasyo.gatherspace.domain

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

sealed interface DomainResponse<out T>

data class Success<T>(val data: T) : DomainResponse<T>

sealed class Error(open val message: String) : DomainResponse<Nothing>

open class DomainError(override val message: String) : Error(message)

data object AuthError : Error("Token not valid")

inline fun <T, R> DomainResponse<T>.onSuccess(onSuccess: (T) -> R): DomainResponse<T> {
    if (this is Success) {
        onSuccess(data)
    }
    return this
}

inline fun <T, R> DomainResponse<T>.onError(onError: (message: String) -> R): DomainResponse<T> {
    if (this is Error) {
        onError(message)
    }
    return this
}

inline fun <T> DomainResponse<T>.onAuthError(onAuthError: () -> Unit): DomainResponse<T> {
    if (this is AuthError) {
        onAuthError()
    }
    return this
}

inline fun <T, R> DomainResponse<T>.fold(
    onSuccess: (T) -> R,
    onDomainError: (message: String) -> R,
    onAuthError: () -> R
): R {
    return when (this) {
        AuthError -> onAuthError()
        is DomainError -> onDomainError(message)
        is Success -> onSuccess(data)
    }
}