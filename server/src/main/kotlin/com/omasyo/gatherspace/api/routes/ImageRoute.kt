package com.omasyo.gatherspace.api.routes

import com.omasyo.gatherspace.utils.ImageDirPath
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File


@Resource("/images")
class Images {
    @Resource("/{id}")
    class Id(val id: String, val parent: Images)
}

fun Application.imageRoute() {
    routing {
        get<Images.Id> { imageId ->
            val imageFile = File(ImageDirPath, imageId.id)
            call.respondBytes(imageFile.readBytes())

//            repository.getUserById(user.id)?.let {
//                call.respond(_root_ide_package_.io.ktor.http.HttpStatusCode.OK, it)
//            } ?: call.respond(
//                _root_ide_package_.com.omasyo.gatherspace.models.response.ErrorResponse(
//                    statusCode = _root_ide_package_.io.ktor.http.HttpStatusCode.NotFound.value,
//                    message = "User Not Found",
//                )
//            )
        }
    }
}