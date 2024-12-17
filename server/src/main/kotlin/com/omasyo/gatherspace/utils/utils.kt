package com.omasyo.gatherspace.utils

import com.omasyo.gatherspace.data.DatabaseResponse
import com.omasyo.gatherspace.models.response.ErrorResponse
import com.omasyo.gatherspace.network.Api.IMAGE_URL_PATH
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.io.Buffer
import kotlinx.io.copyTo
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.outputStream
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

const val ImageDirPath = "images"

fun getImagePath(imageId: String?): String? {
    return imageId?.let { "${IMAGE_URL_PATH}$it" }
}

suspend inline fun ApplicationCall.respond(error: ErrorResponse) {
    respond(
        status = HttpStatusCode.fromValue(error.statusCode),
        message = error
    )
}

fun DatabaseResponse.Failure.toErrorResponse(): ErrorResponse {
    return ErrorResponse(
        statusCode = statusCode,
        message = message
    )
}

@OptIn(ExperimentalUuidApi::class)
fun createImageFile(buffer: Buffer): String {
    val randomId = Uuid.random().toHexString()
    val imageDir = Files.createDirectories(Path(ImageDirPath))
    val file = Files.createFile(imageDir.resolve(randomId))
    buffer.copyTo(file.outputStream())
    return randomId
}