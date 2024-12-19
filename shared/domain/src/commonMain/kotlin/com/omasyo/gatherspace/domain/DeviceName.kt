package com.omasyo.gatherspace.domain

import kotlinx.datetime.LocalDateTime

expect fun getDeviceName(): String

fun LocalDateTime.formatDateTime(): String {
    return formatDate() + ' ' + formatTime()
}