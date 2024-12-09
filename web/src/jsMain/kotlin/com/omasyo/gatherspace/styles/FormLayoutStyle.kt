package com.omasyo.gatherspace.styles

import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.AlignContent
import com.varabyte.kobweb.compose.css.alignContent
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.JustifyContent

object FormLayoutStyle : StyleSheet() {
    val formPage by style {
        width(100.percent)
        height(100.vh)

        display(DisplayStyle.Flex)
//        alignItems(AlignItems.Center)
        alignContent(AlignContent.Center)
        justifyContent(JustifyContent.Center)
//        borderLeft {
//            width = 1.px
//            style = LineStyle.Solid
//            color = lightDark(onSurfaceVariantDark, onSurfaceVariantLight)
//        }
//        borderRight {
//            width = 1.px
//            style = LineStyle.Solid
//            color = lightDark(onSurfaceVariantDark, onSurfaceVariantLight)
//        }
    }

    val formContent by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
//        alignItems(AlignItems.Center)
        alignContent(AlignContent.Center)
        justifyContent(JustifyContent.Center)
        gap(16.px)
        maxWidth(400.px)
    }
}