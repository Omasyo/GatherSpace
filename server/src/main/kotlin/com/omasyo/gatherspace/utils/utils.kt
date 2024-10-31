package com.omasyo.gatherspace.utils

import com.omasyo.gatherspace.models.ErrorResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend inline fun ApplicationCall.respondError(error: ErrorResponse) {
    respond(
        status = HttpStatusCode.fromValue(error.statusCode),
        message = error
    )
}