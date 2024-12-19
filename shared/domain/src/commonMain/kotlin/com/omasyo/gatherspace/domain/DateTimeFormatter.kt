package com.omasyo.gatherspace.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char

fun LocalDateTime.formatDate(): String =
    format(
        LocalDateTime.Format {
            dayOfMonth()
            char(' ')
            monthName(MonthNames.ENGLISH_ABBREVIATED)
            char(' ')
            year()
        }
    )


fun LocalDateTime.formatTime(): String =
    format(
        LocalDateTime.Format {
            hour()
            char(':')
            minute()
        }
    )
