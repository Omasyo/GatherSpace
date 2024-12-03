package com.omasyo.gatherspace.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
    placeholder: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = textStyle.merge(color = LocalContentColor.current),
        modifier = modifier,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        minLines = minLines,
        maxLines = maxLines,
        cursorBrush = SolidColor(LocalContentColor.current),
        visualTransformation = if (keyboardOptions.keyboardType == KeyboardType.Password)
            PasswordVisualTransformation() else VisualTransformation.None,
    ) { innerTextField ->
        if (value.isEmpty() && placeholder != null) {
            Text(
                placeholder,
                style = textStyle.copy(LocalContentColor.current.copy(0.5f))
            )
        }
        innerTextField()
    }
}