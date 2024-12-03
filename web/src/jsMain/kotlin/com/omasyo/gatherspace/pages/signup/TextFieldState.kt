package com.omasyo.gatherspace.pages.signup

data class TextFieldState(
    val value: String,
    val errorMessage: String? = null,
) {
    val isError get() = errorMessage != null
}
