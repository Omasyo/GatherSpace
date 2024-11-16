package com.omasyo.gatherspace.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun LoginPlaceholder(
    modifier: Modifier = Modifier,
    onLoginTap: () -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Button(onClick = onLoginTap) {
            Text("Login")
        }
    }
}
