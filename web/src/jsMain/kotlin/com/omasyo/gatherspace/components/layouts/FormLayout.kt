package com.omasyo.gatherspace.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasyo.gatherspace.styles.FormLayoutStyle
import com.omasyo.gatherspace.styles.HomeLayoutStyle
import kotlinx.browser.document
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div

@Composable
fun FormLayout(
    title: String,
    content: @Composable () -> Unit
) {
    Style(HomeLayoutStyle)
    LaunchedEffect(title) {
        document.title = "GatherSpace - $title"
    }
    Div(attrs = {
        classes(FormLayoutStyle.form)
    }) {
        content()
    }
}