package com.omasyo.gatherspace.components.layouts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.graphics.Colors
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.textAlign
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Text
import kotlin.time.Duration.Companion.seconds

object BaseLayoutStyle : StyleSheet() {
    val snackbar by style {
        visibility(Visibility.Hidden)
        minWidth(250.px)
        marginLeft((-125).px)
        backgroundColor(Colors.Red.copyf(alpha = 0.5f))
        color(Colors.White)
        textAlign(TextAlign.Center)
        padding(16.px)
        position(Position.Fixed)
        zIndex(1)
        left(50.percent)
        bottom(30.px)
        fontSize(17.px)
    }

    val show by style {
        visibility(Visibility.Visible)
    }

    private val fadeIn by keyframes {
        from {
            bottom(0.px)
            opacity(0)
        }
        to {
            bottom(30.px)
            opacity(1)
        }
    }

    private val fadeOut by keyframes {
        from {
            bottom(30.px)
            opacity(1)
        }
        to {
            bottom(0.px)
            opacity(0)
        }
    }

    init {
        "#snackbar" {
            visibility(Visibility.Hidden)
            minWidth(250.px)
            marginLeft((-125).px)
            backgroundColor(surfaceVariantDark)
            color(onSurfaceVariantDark)
            textAlign(TextAlign.Center)
            padding(16.px)
            position(Position.Fixed)
            zIndex(1)
            left(50.percent)
            bottom(30.px)
            fontSize(17.px)
        }

        "#snackbar.show" {
            visibility(Visibility.Visible)
            animation(fadeOut) {
                duration(0.5.s)
                delay(2.5.s)
            }
            animation(fadeIn) {
                duration(0.5.s)
            }

            property("animation", "${fadeIn.name} 0.5s, ${fadeOut.name} 0.5s 2.5s")
        }
    }
}

@Composable
fun BaseLayout(
    title: String,
    content: @Composable () -> Unit
) {
    Style(BaseLayoutStyle)
    LaunchedEffect(title) {
        document.title = title
    }
    Div {
        content()
        Div(
            attrs = {
                id("snackbar")
            }
        ) {
            Text("Hello New World")
        }
    }
}

fun showSnackbar(
    message: String,
) {
    val bar = document.getElementById("snackbar")!!
    bar.textContent = message
    bar.className = "show"
    window.setTimeout({
        bar.className = bar.className.replace("show", "")
    }, 3000)
}