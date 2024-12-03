package com.omasyo.gatherspace.styles

import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.borderRight
import org.jetbrains.compose.web.css.*

object HomeLayoutStyle : StyleSheet() {
    val homeLayout by style {
        width(100.percent)
        height(100.percent)
    }

    val header by style {
//        alignSelf(AlignSelf.Center)
        padding(8.px, 16.px)
        width(100.percent)
        borderBottom {
            width = 1.px
            style = LineStyle.Solid
            color = lightDark(onSurfaceVariantDark, onSurfaceVariantLight)
        }
        position(Position.Absolute)
    }

    val contentWrapper by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Row)
        width(100.percent)
        height(100.percent)
        paddingTop(56.px)
    }

    val sidebar by style {
        height(100.percent)
        borderRight {
            width(1.px)
            style(LineStyle.Solid)
            color(lightDark(onSurfaceVariantDark, onSurfaceVariantDark))
        }
        media(mediaMaxWidth(1000.px)) {
            self style {
                width(100.percent)
            }
        }
        media(mediaMinWidth(1000.px)) {
            self style {
                minWidth(360.px)
            }
        }
    }

    val content by style {
        minWidth(400.px)
        flexGrow(1)
        height(100.percent)
    }

    val hideOnSmall by style {
        media(mediaMaxWidth(600.px)) {
            self style {
                display(DisplayStyle.None)
            }
        }
    }

    val hideOnMid by style {
        media(mediaMaxWidth(1000.px)) {
            self style {
                display(DisplayStyle.None)
            }
        }
    }
}