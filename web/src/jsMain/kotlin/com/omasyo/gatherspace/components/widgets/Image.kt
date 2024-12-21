package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.styles.MainStyle
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLImageElement

@Composable
fun Image(
    imageUrl: String?,
    placeholder: String,
    contentDescription: String? = null,
    attrs: AttrBuilderContext<HTMLImageElement>? = null,
) {
    Img(
        src = imageUrl ?: placeholder,
        alt = contentDescription ?: "",
        attrs = {
            classes(MainStyle.image)
            attrs?.invoke(this)
        }
    )
}