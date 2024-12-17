package com.omasyo.gatherspace.styles

import com.varabyte.kobweb.compose.css.AlignContent
import com.varabyte.kobweb.compose.css.alignContent
import org.jetbrains.compose.web.css.*

object FormLayoutStyle : StyleSheet() {
    val formPage by style {
        width(100.percent)
        height(100.vh)

        display(DisplayStyle.Flex)
        alignContent(AlignContent.Center)
        justifyContent(JustifyContent.Center)
    }

    val formContent by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        alignContent(AlignContent.Center)
        justifyContent(JustifyContent.Center)
        gap(16.px)
        maxWidth(400.px)
    }


}