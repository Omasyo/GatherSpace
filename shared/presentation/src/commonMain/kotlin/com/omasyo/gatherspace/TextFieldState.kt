package com.omasyo.gatherspace

data class TextFieldState(
    val value: String,
    val errorMessage: String? = null,
) {
    val isError get() = errorMessage != null
}