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
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.Alignment
import com.omasyo.gatherspace.ui.components.Image
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.gatherspace
import gatherspace.composeapp.generated.resources.user_placeholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onProfileTap: () -> Unit,
    onCreateRoomTap: () -> Unit,
    isAuthenticated: Boolean,
    isExpanded: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8f.dp),
    ) {
        Image(
            Res.drawable.gatherspace,
            modifier = Modifier.height(24.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isAuthenticated) {
            IconButton(onClick = onCreateRoomTap) {
                Icon(Icons.Default.Add, null)
            }
            if (isExpanded) {
                Text("Username", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(end = 4f.dp))
            }
        }
        Image(
            "todo",
            placeholder = Res.drawable.user_placeholder,
            modifier = Modifier
                .padding(top = 2f.dp)
                .size(40f.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = onProfileTap)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    GatherSpaceTheme(false) {
        TopBar(onCreateRoomTap = {}, onProfileTap = {}, isAuthenticated = true, isExpanded = true)
    }
}