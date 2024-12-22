package com.omasyo.gatherspace.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.domain.formatDateTime
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import com.omasyo.gatherspace.ui.components.*
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.user_placeholder
import kotlinx.datetime.LocalDateTime
import kotlinx.io.Buffer
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    viewModel: ProfileViewModel = dependencyProvider {
        viewModel {
            ComposeProfileViewModel(authRepository, userRepository)
        }
    }
) {

    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strokeWidth = 0.5f.dp
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        ProfileScreen(
            modifier = Modifier.widthIn(max = 800.dp).drawBehind {
                drawLine(
                    borderColor,
                    Offset(0f, 0f),
                    Offset(0f, size.height),
                    strokeWidth.toPx()
                )
                drawLine(
                    borderColor,
                    Offset(size.width, 0f),
                    Offset(size.width, size.height),
                    strokeWidth.toPx()
                )
            },
            onBackTap = onBackTap,
            userDetails = viewModel.userDetails.collectAsStateWithLifecycle().value,
            sessions = viewModel.userSessions.collectAsStateWithLifecycle().value,
            updateImage = viewModel::updateImage,
            onLogoutTap = viewModel::logout,
            onSessionLogoutTap = viewModel::logoutSession,
            onEventReceived = viewModel::onEventReceived,
            state = viewModel.state.collectAsStateWithLifecycle().value,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    userDetails: UserDetails?,
    sessions: List<UserSession>,
    updateImage: (Buffer) -> Unit,
    onLogoutTap: () -> Unit,
    onSessionLogoutTap: (UserSession) -> Unit,
    onEventReceived: (ProfileScreenEvent) -> Unit,
    state: ProfileScreenState
) {
    val sheetState = rememberModalBottomSheetState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = onBackTap
                    ) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text("Profile")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16f.dp, vertical = 72f.dp),
            verticalArrangement = Arrangement.spacedBy(8f.dp),
            horizontalAlignment = Alignment.Start
        ) {

            UserDetailsSection(
                userDetails = userDetails,
                onSetImage = { showBottomSheet = true },
                isRefreshing = false
            )

            HorizontalDivider()
            Text(
                text = "Active Sessions",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8f.dp)
            ) {
                for (session in sessions) {
                    SessionDetails(
                        sessionDetails = session,
                        onLogoutTap = { onSessionLogoutTap(session) },
                    )
                }
            }
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
                        text = "Take a picture",
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

    LaunchedEffect(state) {
        when (val event = state.event) {
            ProfileScreenEvent.AuthError -> {
                snackbarHostState.showSnackbar("Auth error")
            }

            is ProfileScreenEvent.Error -> {
                snackbarHostState.showSnackbar(event.message)
            }

            ProfileScreenEvent.ImageUpdated -> {
                snackbarHostState.showSnackbar("Image updated")
            }

            ProfileScreenEvent.Logout -> {
                onBackTap()
            }

            is ProfileScreenEvent.SessionLogout -> {
                snackbarHostState.showSnackbar("Logout of device successful")
            }

            ProfileScreenEvent.None -> Unit
        }
        onEventReceived(state.event)
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
            }
        }
        Spacer(Modifier.width(16f.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(16f.dp, Alignment.CenterVertically),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = userDetails?.username ?: "",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Created: ${userDetails?.created?.formatDateTime()}"
            )
        }
    }
}

@Composable
fun SessionDetails(
    modifier: Modifier = Modifier,
    sessionDetails: UserSession,
    onLogoutTap: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                sessionDetails.deviceName,
                style = MaterialTheme.typography.titleMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            Text(
                "Recently accessed: ${sessionDetails.lastAccessed.formatDateTime()}",
                style = MaterialTheme.typography.bodyMedium,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
        TextButton(onClick = onLogoutTap) {
            Text("Logout")
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
            sessions = List(5) {
                UserSession(
                    userId = 9225,
                    deviceId = 1125,
                    deviceName = "Wesley Vaughan",
                    created = LocalDateTime(1, 1, 1, 1, 1),
                    lastAccessed = LocalDateTime(1, 1, 1, 1, 1)
                )
            },
            onLogoutTap = {},
            updateImage = {},
            onSessionLogoutTap = {},
            onEventReceived = {},
            state = ProfileScreenState.Initial
        )
    }
}