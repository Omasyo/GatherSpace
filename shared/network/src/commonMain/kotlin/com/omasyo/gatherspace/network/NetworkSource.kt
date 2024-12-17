package com.omasyo.gatherspace.network

import io.github.aakira.napier.Napier
import io.ktor.client.call.*
import io.ktor.client.statement.*
import io.ktor.http.*

interface NetworkSource {
    val tag: String
        get() = this::class.simpleName!!
}

internal suspend inline fun <reified T> NetworkSource.mapResponse(
    exec: () -> HttpResponse
): Result<T> =
    try {
        val response = exec()

        Napier.i(tag = tag) { "Made request ${response.request.method} ${response.request.url}" }
        Napier.i(tag = tag) { "Received: $response ${response.status}" }
        Napier.v("Received: ${response.bodyAsText()}", tag = tag)
        if (response.status.isSuccess()) {
            val content: T = response.body()
            Result.success(content)

        } else {
            Napier.e("Response error - ${response.bodyAsText()}", tag = tag)

            Result.failure(NetworkException(response.body()))

        }
    } catch (e: Exception) {
        Napier.e(throwable = e, tag = tag) { "Exception ${e.message}" }
        Result.failure(e)
    }