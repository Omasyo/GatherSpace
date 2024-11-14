package com.omasyo.gatherspace.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class TextFieldState(
    val value: String,
    val errorMessage: String? = null,
) {
    val isError get() = errorMessage != null
}

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = { hint?.let { Text(it) } },
        supportingText = { supportingText?.let { Text(it) } },
        isError = isError,
    )
}