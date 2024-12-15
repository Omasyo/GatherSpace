package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.io.Buffer

interface PhotoTakerScope {
    fun takePhoto()
}

@Composable
expect fun PhotoTaker(
    onComplete: (Buffer) -> Unit,
    content: @Composable PhotoTakerScope.() -> Unit
)