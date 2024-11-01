package com.omasyo.gatherspace.models.routes

import io.ktor.resources.*

@Resource("/session")
class Session(val deviceId: Int? = null)