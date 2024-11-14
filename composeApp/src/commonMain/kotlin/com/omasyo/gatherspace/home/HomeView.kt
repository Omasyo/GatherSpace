package com.omasyo.gatherspace.home

import com.omasyo.gatherspace.BackHandler
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.omasyo.gatherspace.*
import com.omasyo.gatherspace.createroom.CreateRoomRoute
import com.omasyo.gatherspace.models.response.Room

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    onAuthError: () -> Unit,
    onLoginTap: () -> Unit,
    onProfileTap: () -> Unit,
    homeViewModel: HomeViewModel = dependencyProvider {
        viewModel { HomeViewModel(roomRepository, userRepository) }
    },
) {
    HomeView(
        modifier = modifier,
        isAuthenticated = isAuthenticated,
        onAuthError = onAuthError,
        onLoginTap = onLoginTap,
        onProfileTap = onProfileTap,
        onRetry = homeViewModel::refreshRooms,
        roomsState = homeViewModel.rooms.collectAsStateWithLifecycle().value,
    )
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    onAuthError: () -> Unit,
    onLoginTap: () -> Unit,
    onProfileTap: () -> Unit,
    onRetry: () -> Unit,
    roomsState: UiState<List<Room>>,

    ) {
    val navigator = rememberListDetailPaneScaffoldNavigator<HomeRoutes>(
        calculateCustomPaneScaffoldDirective(
            currentWindowAdaptiveInfo()
        )
    )
    val onCreateRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, CreateRoom) }
    BackHandler(enabled = navigator.canNavigateBack(), onBack = { navigator.navigateBack() })


    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data("https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Flag_of_Nigeria.svg/255px-Flag_of_Nigeria.svg.png")
                        .crossfade(true).build(),
                    null
                )
                Column {
                    if (navigator.scaffoldValue.primary == PaneAdaptedValue.Hidden) {
                        TopBar(
                            modifier = Modifier.clickable { onLoginTap() },
                            onProfileTap = onProfileTap,
                            onCreateRoomTap = onCreateRoomTap,
                            isAuthenticated = isAuthenticated,
                        )
                    }
                    RoomsView(
                        onRoomTap = {
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(it.id))
                        },
                        onRetry = onRetry,
                        state = roomsState,
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val route = navigator.currentDestination?.content
                if (route != null) {
                    when (route) {
                        CreateRoom -> CreateRoomRoute(
                            onRoomCreated = { id ->
                                onRetry()
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(id))
                            },
                            onAuthError = onAuthError
                        )

                        is RoomRoute -> MessagePanel(
                            onBackTap = { navigator.navigateBack() },
                            roomId = route.id
                        )

                        Search -> TODO()
                    }
                } else {
                    Column {
                        TopBar(
                            onProfileTap = onProfileTap,
                            onCreateRoomTap = onCreateRoomTap,
                            isAuthenticated = isAuthenticated
                        )
                        if (isAuthenticated) {
                        } else {
                            LoginPlaceholder(
                                modifier = Modifier.fillMaxSize(),
                                onLoginTap = onLoginTap
                            )
                        }
                    }
                }
            }
        },
        extraPane = {
            AnimatedPane {
                Box(Modifier.fillMaxSize().background(Color.Cyan))
            }
        },
    )
}

@Composable
private fun LoginPlaceholder(
    modifier: Modifier = Modifier,
    onLoginTap: () -> Unit
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Button(onClick = onLoginTap) {
            Text("Login")
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {

        HomeView(
            modifier = Modifier.fillMaxSize(),
            onLoginTap = {},
            onRetry = {},
            isAuthenticated = true,
            onAuthError = {},
            onProfileTap = {},
            roomsState = UiState.Success(rooms),
        )
    }
}

private val rooms = List(10) {
    Room(id = it, name = "Maribel Ganes")
}
