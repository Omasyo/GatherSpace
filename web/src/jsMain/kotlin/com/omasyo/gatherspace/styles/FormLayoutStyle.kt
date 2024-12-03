package com.omasyo.gatherspace.styles

import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.borderLeft
import com.varabyte.kobweb.compose.css.borderRight
import org.jetbrains.compose.web.css.*

object FormLayoutStyle : StyleSheet() {
    val form by style {
        maxWidth(800.px)
        borderLeft {
            width = 1.px
            style = LineStyle.Solid
            color = lightDark(onSurfaceVariantDark, onSurfaceVariantLight)
        }
        borderRight {
            width = 1.px
            style = LineStyle.Solid
            color = lightDark(onSurfaceVariantDark, onSurfaceVariantLight)
        }
    }
}