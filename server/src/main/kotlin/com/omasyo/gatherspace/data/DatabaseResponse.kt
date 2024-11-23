package com.omasyo.gatherspace.data

sealed interface DatabaseResponse<out T> {
    class Success<T>(val data: T) : DatabaseResponse<T>

    class Failure(val message: String, val statusCode: Int) : DatabaseResponse<Nothing>
}