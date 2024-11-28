package com.omasyo.gatherspace.domain

import com.omasyo.gatherspace.network.NetworkException
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char

expect fun getDeviceName(): String

fun LocalDateTime.formatDateTime(): String {
    return formatDate() + ' ' + formatTime()
}

fun LocalDateTime.formatDate(): String {
    return format(
        LocalDateTime.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }
    )
}

fun LocalDateTime.formatTime(): String {
    return format(
        LocalDateTime.Format {
            hour()
            char(':')
            minute()
        }
    )
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