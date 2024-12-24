package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.Composable
import kotlinx.io.Buffer

interface ImageChooserScope {
    fun chooseImage()
}

@Composable
expect fun ImageChooser(
    onComplete: (Buffer) -> Unit,
    content: @Composable ImageChooserScope.() -> Unit
)