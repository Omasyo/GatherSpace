package com.omasyo.gatherspace.components.layouts

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.styles.FormLayoutStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.dom.Div

@Composable
fun FormLayout(
    title: String,
    content: @Composable () -> Unit
) {
    Style(FormLayoutStyle)
    BaseLayout("GatherSpace - $title") {
        Div(attrs = {
            classes(FormLayoutStyle.formPage)
        }) {
            Div(
                attrs = {
                    classes(FormLayoutStyle.formContent)
                }
            ) {
                content()
            }
        }
    }
}