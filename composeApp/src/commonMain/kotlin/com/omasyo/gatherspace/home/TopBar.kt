package com.omasyo.gatherspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onProfileTap: () -> Unit,
    onCreateRoomTap: () -> Unit,
    isAuthenticated: Boolean,
) {
    TopAppBar(
        modifier = modifier,
        title = { Text("GatherSpace") },
        actions = {
            if(isAuthenticated) {
                IconButton(onClick = onCreateRoomTap) {
                    Icon(Icons.Default.Add, null)
                }
            }
            Box(
                modifier = Modifier.clickable(onClick = onProfileTap).size(48.dp).clip(CircleShape)
                    .background(Color.Red)
            )
        })
}