package com.omasyo.gatherspace.ui.components

data class TextFieldState(
    val value: String,
    val errorMessage: String? = null,
) {
    val isError get() = errorMessage != null
}