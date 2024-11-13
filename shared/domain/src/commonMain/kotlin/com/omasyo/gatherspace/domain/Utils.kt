package com.omasyo.gatherspace.domain

import com.omasyo.gatherspace.network.NetworkException

expect fun getDeviceName(): String

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