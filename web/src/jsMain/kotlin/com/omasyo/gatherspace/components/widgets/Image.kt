package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.styles.MainStyle
import com.varabyte.kobweb.compose.css.height
import com.varabyte.kobweb.compose.css.width
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.toAttrs
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.dom.AttrBuilderContext
import org.jetbrains.compose.web.dom.Img
import org.w3c.dom.HTMLImageElement

@Composable
fun Image(
    imageUrl: String?,
    placeholder: String,
    size: Int,
    contentDescription: String? = null,
    attrs: AttrBuilderContext<HTMLImageElement>? = null,
) {
    Img(
        src = imageUrl ?: placeholder,
        alt = contentDescription ?: "",
        attrs = {
            classes(MainStyle.image)
            width(size)
            height(size)
            attrs?.invoke(this)
        }
    )
}