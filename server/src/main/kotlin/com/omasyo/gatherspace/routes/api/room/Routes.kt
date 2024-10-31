package com.omasyo.gatherspace.routes.api.room

import io.ktor.resources.*

@Resource("/rooms")
class Rooms {
    @Resource("/{id}")
    class Id(val id: Int, val parent: Rooms)
}

@Resource("/members")
class Members(val room: Id) {
    @Resource("/{id}")
    class Id(val id: Int, val parent: Rooms)
}