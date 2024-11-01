package com.omasyo.gatherspace.domain

import com.omasyo.gatherspace.network.NetworkException

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