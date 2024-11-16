package com.omasyo.gatherspace.home

import com.omasyo.gatherspace.BackHandler
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.*
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme

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

    Surface(modifier) {
        Home(
            modifier = Modifier.fillMaxSize(),
            isAuthenticated = isAuthenticated,
            topBar = {
                TopBar(
                    modifier = Modifier.padding(horizontal = 16f.dp, vertical = 8f.dp),
                    onProfileTap = onProfileTap,
                    onCreateRoomTap = onCreateRoomTap,
                    isAuthenticated = isAuthenticated
                )

            },
            roomsList = {
                RoomsList(
                    onRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(it)) },
                    onRetry = onRetry,
                    state = roomsState,
                )
            },
            roomsGrid = {
//                Box(Modifier.fillMaxSize().background(Color.Green))
            },
            roomView = { roomId ->
                RoomPanel(
                    roomId = roomId,
                    onBackTap = navigator::navigateBack,
                )
            },
            createRoomView = {
//                Box(Modifier.fillMaxSize().background(Color.Yellow))
            },
            loginPlaceholder = {
//                Box(Modifier.fillMaxSize().background(Color.Red))
            },
            navigator = navigator
        )
    }
}

@Preview
@Composable
private fun Preview() {
    GatherSpaceTheme {

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
