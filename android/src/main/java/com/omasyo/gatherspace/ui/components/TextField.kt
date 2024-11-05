package com.omasyo.gatherspace.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun PTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onSubmit: (() -> Unit)? = null,
    color: Color? = null,
    focusRequester: FocusRequester? = null,
    modifier: Modifier = Modifier
) {

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = LocalContentColor.current,
            backgroundColor = LocalContentColor.current
        )
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = color ?: LocalContentColor.current
            ),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (onSubmit != null) {
                        onSubmit()
                    }
                }
            ),
            cursorBrush = SolidColor(LocalContentColor.current),
            modifier = modifier
                .clip(MaterialTheme.shapes.small)
                .fillMaxWidth()
                .padding(vertical = 8f.dp)
                .padding(horizontal = 16f.dp, vertical = 8f.dp)
                .run { if (focusRequester != null) then(Modifier.focusRequester(focusRequester)) else this }
        )
//    { innerTextField ->
//        if (value.isEmpty()) {
//            Text(
//                "????",
//                textAlign = TextAlign.Center,
//                color = LocalContentColor.current.copy(0.7f),
//                modifier = Modifier.fillMaxWidth()
//            )
//        }
//        innerTextField()
//    }
    }
}