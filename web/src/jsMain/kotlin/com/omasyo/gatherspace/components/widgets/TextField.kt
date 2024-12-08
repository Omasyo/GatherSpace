package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    autoCapitalize: String = "off",
//    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    rows: Int = 1,
) {
    if (singleLine) {
        if (isPassword) {
            PasswordInput(
                value = value
            ) {
                onInput { onValueChange(it.value) }
                placeholder?.let { placeholder(it) }
            }
        } else {
            TextInput(
                value = value
            ) {
                onInput { onValueChange(it.value) }
                placeholder?.let { placeholder(it) }
                attr("autocapitalize", autoCapitalize)
            }
        }
    } else {
        TextArea(
            value = value
        ) {
            onInput { onValueChange(it.value) }
            placeholder?.let { placeholder(it) }
            attr("autocapitalize", autoCapitalize)
            rows(rows)
        }
    }
}

//@Composable
//fun FormTextField(
//    title: String,
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String? = null,
//    autoCapitalize: String = "off",
//    singleLine: Boolean = true,
//    rows: Int = 1,
//) {
//    Div(attrs = {}) {
//        Text(title)
//        TextField(
//            value = value,
//            onValueChange = onValueChange,
//            placeholder = placeholder,
//            autoCapitalize = autoCapitalize,
//            singleLine = singleLine,
//            rows = rows
//        )
//    }
//}