package com.omasyo.gatherspace

import androidx.compose.runtime.Composable

@Composable
expect fun BackHandler(enabled: Boolean, onBack: () -> Unit)