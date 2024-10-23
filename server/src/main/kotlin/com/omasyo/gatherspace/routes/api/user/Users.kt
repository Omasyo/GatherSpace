package com.omasyo.gatherspace.routes.api.user

import io.ktor.resources.*

@Resource("/users")
class Users {
    @Resource("/{id}")
    class Id(val id: Int, val parent: Users)
}