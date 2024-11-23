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
import com.omasyo.gatherspace.createroom.CreateRoomRoute
import com.omasyo.gatherspace.home.layout.HomeLayout
import com.omasyo.gatherspace.home.layout.calculateCustomPaneScaffoldDirective
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.ui.components.LoginPlaceholder
import com.omasyo.gatherspace.ui.theme.*

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    onAuthError: () -> Unit,
    onLoginTap: () -> Unit,
    onProfileTap: () -> Unit,
    homeViewModel: HomeViewModel = dependencyProvider {
        viewModel { HomeViewModel(roomRepository, userRepository, authRepository) }
    },
) {
    HomeView(
        modifier = modifier,
        isAuthenticated = isAuthenticated,
        onAuthError = onAuthError,
        onLoginTap = onLoginTap,
        onProfileTap = onProfileTap,
        onRefresh = homeViewModel::refreshRooms,
        allRoomsState = homeViewModel.allRooms.collectAsStateWithLifecycle().value,
        userRoomsState = homeViewModel.userRooms.collectAsStateWithLifecycle().value,
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
    onRefresh: () -> Unit,
    allRoomsState: UiState<List<Room>>,
    userRoomsState: UiState<List<Room>>,

    ) {
    val navigator = rememberListDetailPaneScaffoldNavigator<HomeRoutes>(
        calculateCustomPaneScaffoldDirective(
            currentWindowAdaptiveInfo()
        )
    )
    val onCreateRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, CreateRoom) }
    BackHandler(enabled = navigator.canNavigateBack(), onBack = { navigator.navigateBack() })

    Surface(modifier) {
        HomeLayout(
            modifier = Modifier.fillMaxSize(),
            isAuthenticated = isAuthenticated,
            topBar = { isExpanded ->
                TopBar(
                    modifier = Modifier.padding(horizontal = 16f.dp, vertical = 8f.dp),
                    onProfileTap = onProfileTap,
                    onCreateRoomTap = onCreateRoomTap,
                    isAuthenticated = isAuthenticated,
                    isExpanded = isExpanded
                )

            },
            roomsList = {
                RoomsList(
                    onRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(it)) },
                    onRetry = onRefresh,
                    state = if (isAuthenticated) userRoomsState else allRoomsState,
                )
            },
            roomsGrid = {
                RoomsGrid(
                    onRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(it)) },
                    onRetry = onRefresh,
                    state = allRoomsState,
                )
            },
            roomView = { roomId ->
                RoomPanel(
                    roomId = roomId,
                    isAuthenticated = isAuthenticated,
                    onRegisterTap = onLoginTap,
                    onJoin = onRefresh,
                    onBackTap = navigator::navigateBack,
                )
            },
            createRoomView = {
                CreateRoomRoute(
                    onBackTap = { navigator.navigateBack() },
                    onRoomCreated = {
                        onRefresh()
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomRoute(it))
                    },
                    onAuthError = onAuthError,
                )
            },
            loginPlaceholder = {
                LoginPlaceholder(onLoginTap = onLoginTap)
            },
            navigator = navigator
        )
    }
}

@Preview
@Composable
private fun Preview() {
    GatherSpaceTheme(
//        colorScheme = lightScheme
    ) {

        HomeView(
            modifier = Modifier.fillMaxSize(),
            onLoginTap = {},
            onRefresh = {},
            isAuthenticated = true,
            onAuthError = {},
            onProfileTap = {},
            allRoomsState = UiState.Success(rooms),
            userRoomsState = UiState.Success(rooms)
        )
    }
}

private val rooms = List(10) {
    Room(id = it, name = "Maribel Ganes", imageUrl = "")
}
