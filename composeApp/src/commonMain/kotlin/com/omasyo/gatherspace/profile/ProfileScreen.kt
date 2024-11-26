package com.omasyo.gatherspace.profile

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.domain.formatDate
import com.omasyo.gatherspace.home.UiState
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.ui.components.*
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.user_placeholder
import kotlinx.datetime.LocalDateTime
import kotlinx.io.Buffer

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
) {
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    userDetails: UserDetails?,
    sessions: List<Any>,
    updateImage: (Buffer) -> Unit,
    onLogoutTap: () -> Unit,
    onSessionLogoutTap: (Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->

        IconButton(
            onClick = onBackTap,
            modifier = Modifier.padding(innerPadding).padding(top = 16f.dp, start = 8f.dp)
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16f.dp, vertical = 72f.dp),
            verticalArrangement = Arrangement.spacedBy(8f.dp),
            horizontalAlignment = Alignment.Start
        ) {

            UserDetailsSection(
                userDetails = userDetails,
                onSetImage = { showBottomSheet = true },
                isRefreshing = true
            )

            HorizontalDivider()
            //TODO put sessions here
            HorizontalDivider()

            Button(
                onClick = onLogoutTap,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Logout")
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val onComplete = { imageBuffer: Buffer ->
                    updateImage(imageBuffer)
                    showBottomSheet = false
                }

                PhotoTaker(onComplete = onComplete) {
                    BottomSheetMenuItem(
                        text = "Choose from gallery",
                        onTap = ::takePhoto
                    )
                }
                ImageChooser(onComplete = onComplete) {
                    BottomSheetMenuItem(
                        text = "Choose from gallery",
                        onTap = ::chooseImage
                    )
                }

                Spacer(Modifier.height(16f.dp))
            }
        }

    }
}

@Composable
fun UserDetailsSection(
    modifier: Modifier = Modifier,
    userDetails: UserDetails?,
    onSetImage: () -> Unit,
    isRefreshing: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .width(120f.dp)
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.extraLarge)
                .clickable(onClick = onSetImage)
        ) {
            Image(
                imageUrl = userDetails?.imageUrl,
                placeholder = Res.drawable.user_placeholder,
                modifier = Modifier.fillMaxSize()
            )
            if (isRefreshing) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.15f))
                        .fillMaxSize()
                        .padding(16f.dp)
                )
            } else {
                Icon(
                    Icons.Outlined.PhotoCamera, null,
                    tint = Color.White.copy(alpha = 0.6f),
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.15f))
                        .fillMaxSize()
                        .padding(16f.dp)
                )
            }
        }
        Spacer(Modifier.width(16f.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16f.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = "User Profile",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Created: ${userDetails?.created?.formatDate()}"
            )
        }
    }
}

@Composable
fun BottomSheetMenuItem(
    text: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clickable(onClick = onTap)
            .fillMaxWidth()
            .padding(horizontal = 16f.dp, vertical = 16f.dp),
    )
}

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    GatherSpaceTheme(false) {
        ProfileScreen(
            onBackTap = {},
            userDetails = UserDetails(
                id = 5801,
                username = "Ellen Ford",
                imageUrl = null,
                created = LocalDateTime(1, 1, 1, 1, 1),
                modified = LocalDateTime(1, 1, 1, 1, 1)
            ),
            sessions = emptyList(),
            onLogoutTap = {},
            updateImage = {},
            onSessionLogoutTap = {},
        )
    }
}