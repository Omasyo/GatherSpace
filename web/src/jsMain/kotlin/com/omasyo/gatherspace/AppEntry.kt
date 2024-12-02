package com.omasyo.gatherspace

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.styles.MainStyle
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.KobwebApp
import org.jetbrains.compose.web.css.Style

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    Style(MainStyle)
    KobwebApp {
        content()
    }
}