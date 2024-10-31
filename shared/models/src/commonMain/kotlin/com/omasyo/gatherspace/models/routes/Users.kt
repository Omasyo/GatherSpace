package com.omasyo.gatherspace.models.routes

import io.ktor.resources.*

@Resource("/users")
class Users {
    @Resource("/{id}")
    class Id(val id: Int, val parent: Users)
}