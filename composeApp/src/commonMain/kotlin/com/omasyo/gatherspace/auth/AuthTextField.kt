package com.omasyo.gatherspace.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.ui.components.TextField

@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    hint: String,
) {
    Column(modifier = modifier) {
        TextField(
            value = state.value,
            onValueChange = onValueChange,
            placeholder = hint,
            singleLine = true,
            keyboardOptions = keyboardOptions,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1f.dp,
                    color = if (state.isError) MaterialTheme.colorScheme.error else LocalContentColor.current,
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(horizontal = 16f.dp, vertical = 12f.dp),
        )
        state.errorMessage?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8f.dp).padding(top = 2.dp, bottom = 8.dp)
            )
        }
    }
}