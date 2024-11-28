package com.omasyo.gatherspace.home

import com.omasyo.gatherspace.BackHandler
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.*
import com.omasyo.gatherspace.createroom.CreateRoomRoute
import com.omasyo.gatherspace.home.layout.HomeLayout
import com.omasyo.gatherspace.home.layout.HomeLayoutView
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.room.RoomPanel
import com.omasyo.gatherspace.ui.theme.*

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
        onRefresh = homeViewModel::refresh,
        userState = homeViewModel.user.collectAsStateWithLifecycle().value,
        allRoomsState = homeViewModel.allRooms.collectAsStateWithLifecycle().value,
        userRoomsState = homeViewModel.userRooms.collectAsStateWithLifecycle().value,
    )
}


@Composable
fun HomeView(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    onAuthError: () -> Unit,
    onLoginTap: () -> Unit,
    onProfileTap: () -> Unit,
    onRefresh: () -> Unit,
    userState: UiState<UserDetails>,
    allRoomsState: UiState<List<Room>>,
    userRoomsState: UiState<List<Room>>,

    ) {
//    val navigator = rememberListDetailPaneScaffoldNavigator<HomeRoutes>(
//        calculateCustomPaneScaffoldDirective(
//            currentWindowAdaptiveInfo()
//        )
//    )

    var selected by remember { mutableStateOf<HomeLayoutView>(HomeLayoutView.None) }
//    val onCreateRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, CreateRoom) }
//    BackHandler(enabled = navigator.canNavigateBack(), onBack = { navigator.navigateBack() })

    Surface(modifier) {
        HomeLayout(
            modifier = Modifier.fillMaxSize(),
            isAuthenticated = isAuthenticated,
            selectedView = selected,
            onBack = { selected = HomeLayoutView.None },
            topBar = { isExpanded ->
                TopBar(
                    modifier = Modifier
                        .padding(horizontal = 16f.dp, vertical = 8f.dp)
                        .heightIn(48f.dp),
                    onProfileTap = onProfileTap,
                    onLoginTap = onLoginTap,
                    onCreateRoomTap = { selected = HomeLayoutView.CreateRoom },
                    isAuthenticated = isAuthenticated,
                    isExpanded = isExpanded,
                    userState = userState,
                )

            },
            roomsList = {
                RoomsList(
                    onRoomTap = { selected = HomeLayoutView.Room(it) },
                    onJoinRoomTap = { selected = HomeLayoutView.Discover },
                    onRetry = onRefresh,
                    state = if (isAuthenticated) userRoomsState else allRoomsState,
                )
            },
            roomsGrid = {
                RoomsGrid(
                    onRoomTap = { selected = HomeLayoutView.Room(it) },
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
                    onBackTap = { selected = HomeLayoutView.None },
                )
            },
            createRoomView = {
                CreateRoomRoute(
                    onBackTap = { selected = HomeLayoutView.None },
                    onRoomCreated = {
                        onRefresh()
                        selected = HomeLayoutView.Room(it)
                    },
                    onAuthError = onAuthError,
                )
            },
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
            userState = UiState.Loading,
            allRoomsState = UiState.Success(rooms),
            userRoomsState = UiState.Success(rooms)
        )
    }
}

private val rooms = List(10) {
    Room(id = it, name = "Maribel Ganes", imageUrl = "")
}
