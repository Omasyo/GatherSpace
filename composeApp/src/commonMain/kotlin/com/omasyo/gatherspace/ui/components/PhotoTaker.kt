package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.Composable
import kotlinx.io.Buffer

interface PhotoTakerScope {
    fun takePhoto()
}

@Composable
expect fun PhotoTaker(
    onComplete: (Buffer) -> Unit,
    content: @Composable PhotoTakerScope.() -> Unit
)