package com.omasyo.gatherspace

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.omasyo.gatherspace.styles.MainStyle
import com.varabyte.kobweb.core.App
import com.varabyte.kobweb.core.KobwebApp
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import org.jetbrains.compose.web.css.Style

@App
@Composable
fun AppEntry(content: @Composable () -> Unit) {
    LaunchedEffect(Unit) {
        Napier.base(DebugAntilog())
        Napier.i { "Starting Napier" }
    }
    Style(MainStyle)
    KobwebApp {
        content()
    }
}