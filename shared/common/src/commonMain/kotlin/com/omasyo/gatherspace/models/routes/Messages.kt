package com.omasyo.gatherspace.models.routes

import io.ktor.resources.*
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Resource("/messages")
class Messages(
    val room: Rooms.Id,
    val before: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val limit: Int = 50,
)