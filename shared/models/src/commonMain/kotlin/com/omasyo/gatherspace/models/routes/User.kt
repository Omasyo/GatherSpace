package com.omasyo.gatherspace.models.routes

import io.ktor.resources.*

@Resource("/user")
class User {
    @Resource("/rooms")
    class Rooms(val parent: User)
}