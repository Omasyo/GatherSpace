package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.Element

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
    autoCapitalize: String = "off",
    isPassword: Boolean = false,
    singleLine: Boolean = true,
    rows: Int = 1,
    attrs: AttrBuilderContext<Element> = {}
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
                value = value,
            ) {
                onInput { onValueChange(it.value) }
                placeholder?.let { placeholder(it) }
                attr("autocapitalize", autoCapitalize)
                attrs()
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
            attrs()
        }
    }
}