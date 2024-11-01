package com.omasyo.gatherspace.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.*

interface NetworkSource {
    val tag: String
        get() = this::class.simpleName!!
}

internal suspend inline fun <reified T> NetworkSource.mapResponse(
//    expectedStatusCode: HttpStatusCode = HttpStatusCode.OK,
    exec: () -> HttpResponse
): Result<T> =
    try {
        val response = exec()
        print("$tag:makeRequest: Made request ${response.request.url}")
        print("$tag:makeRequest:  Got content ${response.bodyAsText()}")
        if (response.status.isSuccess()) {
            val content: T = response.body()
            Result.success(content)

        } else {
            print("$tag:makeRequest: Error - ${response.bodyAsText()}")

            Result.failure(NetworkException(response.body()))

        }
    } catch (e: Exception) {
        print("$tag:makeRequest: Exception - $e")
        Result.failure(e)
    }