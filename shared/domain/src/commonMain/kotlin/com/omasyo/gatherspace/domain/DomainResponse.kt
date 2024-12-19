package com.omasyo.gatherspace.domain

import com.omasyo.gatherspace.network.NetworkException

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


inline fun <T, R> Result<T>.mapToDomain(
    transform: (T) -> R
): DomainResponse<R> = fold(
    onSuccess = { Success(transform(it)) },
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

fun <T> Result<T>.mapToDomain(): DomainResponse<T> =
    mapToDomain { it }