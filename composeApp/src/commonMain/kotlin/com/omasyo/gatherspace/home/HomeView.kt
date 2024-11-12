package com.omasyo.gatherspace.home

import BackHandler
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.*
import com.omasyo.gatherspace.createroom.CreateRoomScreen
import com.omasyo.gatherspace.models.response.Room

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = dependencyProvider {
        viewModel { HomeViewModel(roomRepository) }
    },
) {
    HomeView(
        modifier, onRetry = homeViewModel::refreshRooms,
        roomsState = homeViewModel.rooms.collectAsStateWithLifecycle().value,
    )
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeView(
    modifier: Modifier = Modifier,

    onRetry: () -> Unit,
    roomsState: UiState<List<Room>>,

    ) {
    val navigator = rememberListDetailPaneScaffoldNavigator<HomeRoutes>()
    BackHandler(enabled = navigator.canNavigateBack(), onBack = { navigator.navigateBack() })

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                RoomsView(
                    onRoomTap = {
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomR(it.id)) },
                    onRetry = onRetry,
                    state = roomsState,
                    onCreateRoomTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, CreateRoom) }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    when (it) {
                        CreateRoom -> CreateRoomScreen(onRoomCreated = { id ->
                            onRetry()
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomR(id))
                        })
                        is RoomR -> MessagePanel(
                            onBackTap = { navigator.navigateBack() },
                            roomId = it.id
                        )

                        Search -> TODO()
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

@Preview
@Composable
private fun Preview() {
    MaterialTheme {

        HomeView(
            modifier = Modifier.fillMaxSize(),
            onRetry = {},
            roomsState = UiState.Success(rooms),
        )
    }
}

private val rooms = List(10) {
    Room(id = it, name = "Maribel Ganes")
}
