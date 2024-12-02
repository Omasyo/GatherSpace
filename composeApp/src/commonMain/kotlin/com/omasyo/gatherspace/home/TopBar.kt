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
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.ui.components.Image
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.gatherspace
import gatherspace.composeapp.generated.resources.user_placeholder
import kotlinx.datetime.LocalDateTime

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onProfileTap: () -> Unit,
    onLoginTap: () -> Unit,
    onCreateRoomTap: () -> Unit,
    isAuthenticated: Boolean,
    isExpanded: Boolean,
    userState: UiState<UserDetails>
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
            when (userState) {
                is UiState.Error -> Unit
                UiState.Loading -> UserDetailsPlaceholder(isExpanded = isExpanded)
                is UiState.Success -> UserDetails(
                    user = userState.data,
                    onProfileTap = onProfileTap,
                    onCreateRoomTap = onCreateRoomTap,
                    isExpanded = isExpanded
                )
            }
        } else {
            TextButton(onClick = onLoginTap) {
                Text("Login")
            }
        }
    }
}

@Composable
private fun UserDetails(
    modifier: Modifier = Modifier,
    user: UserDetails,
    onCreateRoomTap: () -> Unit,
    onProfileTap: () -> Unit,
    isExpanded: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onCreateRoomTap) {
            Icon(Icons.Default.Add, null)
        }
        if (isExpanded) {
            Text(
                text = user.username,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(end = 16f.dp)
            )
        }
        Image(
            user.imageUrl,
            placeholder = Res.drawable.user_placeholder,
            modifier = Modifier
                .padding(top = 2f.dp)
                .size(40f.dp)
                .clip(MaterialTheme.shapes.small)
                .clickable(onClick = onProfileTap)
        )
    }
}

@Composable
private fun UserDetailsPlaceholder(
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (isExpanded) {
            Box(
                modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                    .height(16f.dp)
                    .width(120f.dp)
            )
        }
        Box(
            Modifier
                .padding(top = 2f.dp, start = 16f.dp)
                .size(40f.dp)
                .clip(MaterialTheme.shapes.small)
                .background(MaterialTheme.colorScheme.surfaceVariant)
        )
    }
}

@Preview
@Composable
private fun Preview() {
    GatherSpaceTheme(false) {
        TopBar(
            onCreateRoomTap = {},
            onProfileTap = {},
            onLoginTap = {},
            isAuthenticated = true,
            isExpanded = true,
            userState = UiState.Success(
                UserDetails(
                    id = 2310,
                    username = "Heath Sandoval",
                    imageUrl = null,
                    created = LocalDateTime(1, 1, 1, 1, 1, 1),
                    modified = LocalDateTime(1, 1, 1, 1, 1, 1)
                )
            )
        )
    }
}