package com.omasyo.gatherspace.routes.api.message

import com.omasyo.gatherspace.routes.api.room.Rooms
import io.ktor.resources.*

@Resource("/messages")
class Messages(val room: Rooms.Id) {
    @Resource("/{id}")
    class Id(val id: Int, val parent: Messages)
}