package com.omasyo.gatherspace.models.routes

import io.ktor.resources.*

@Resource("/rooms")
class Rooms {
    @Resource("{id}")
    class Id(val id: Int, val parent: Rooms = Rooms())

    @Resource("members")
    class Members(val room: Id) {
    }
}