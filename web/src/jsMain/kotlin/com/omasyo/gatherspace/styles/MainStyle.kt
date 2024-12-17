package com.omasyo.gatherspace.styles

import com.omasyo.gatherspace.theme.onSurfaceDark
import com.omasyo.gatherspace.theme.onSurfaceLight
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.css.AlignContent
import com.varabyte.kobweb.compose.css.JustifyContent
import com.varabyte.kobweb.compose.ui.graphics.Color
import org.jetbrains.compose.web.css.*

object MainStyle : StyleSheet() {
    init {
        root {
            property("color-scheme", "light dark")
        }
        "*" style {
            fontFamily("Carlito", "sans-serif")
        }
        "h1, h2, h3, h4, h5, h6" style {
            fontFamily("Atkinson Hyperlegible", "sans-serif")
            fontWeight(FontWeight.Bold)
            margin(0.px)
        }
        "p" style {
            margin(8.px, 0.px)
        }
        type("a") + anyLink style {
            textDecorationLine(TextDecorationLine.None)
            color(lightDark(onSurfaceLight, onSurfaceDark))
        }
        type("input") style {
            width(100.percent)
        }

        id("logo") style {
            display(DisplayStyle.Block)
        }
        media(mediaMaxWidth(1000.px)) {
            id("logo") style {
                height(24.px)
            }

            "h1" style {
                fontSize(1.2.cssRem)
            }
        }
        media(mediaMinWidth(1000.px)) {
            id("logo") style {
                height(32.px)
            }
            "h1" style {
                fontSize(1.6.cssRem)
            }
        }
        type("input") style {
            padding(12.px, 20.px)
            display(DisplayStyle.InlineBlock)
            borderRadius(4.px)
            boxSizing("border-box")
        }

        type("textarea") style {
            padding(12.px, 20.px)
            display(DisplayStyle.InlineBlock)
            borderRadius(4.px)
            boxSizing("border-box")
        }
    }

    val center by style {
        display(DisplayStyle.Flex)
        alignContent(AlignContent.Center)
        justifyContent(JustifyContent.Center)
    }

    val customLink by style {
        hover {
            textDecorationLine(TextDecorationLine.Underline)
        }
    }

    val clickable by style {
        borderRadius(32.px)
        padding(4.px, 8.px)

        self + hover style {
            background(lightDark(onSurfaceLight, onSurfaceDark, 0.2f))
        }
        self + active style {
            background(lightDark(onSurfaceLight, onSurfaceDark, 0.4f))
        }
    }

    val filledButton by style {
        padding(8.px, 4.px)
    }

    val image by style {
        display(DisplayStyle.Block)
        borderRadius(8.px)
        objectFit(ObjectFit.Cover)
    }
}

fun lightDark(light: Color.Rgb, dark: Color.Rgb): CSSColorValue {
    return Color("light-dark($light, $dark)")
}

fun lightDark(light: Color.Rgb, dark: Color.Rgb, alpha: Float): CSSColorValue {
    return Color("light-dark(${light.copyf(alpha = alpha)}, ${dark.copyf(alpha = alpha)})")
}