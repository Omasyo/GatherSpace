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
import com.omasyo.gatherspace.CreateRoom
import com.omasyo.gatherspace.HomeRoutes
import com.omasyo.gatherspace.RoomR
import com.omasyo.gatherspace.Search
import com.omasyo.gatherspace.auth.client
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import kotlinx.coroutines.Dispatchers

val messageNetworkSource = MessageNetworkSource(client)
val messageRepository = MessageRepository(messageNetworkSource, Dispatchers.IO)

val roomNetworkSource = RoomNetworkSource(client)
val roomRepository = RoomRepository(roomNetworkSource, Dispatchers.IO)

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel {
        HomeViewModel(
            roomRepository
        )
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
                    onRoomTap = { it -> navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, RoomR(it.id)) },
                    onRetry = onRetry,
                    state = roomsState
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    when (it) {
                        CreateRoom -> TODO()
                        is RoomR -> MessagePanel(
                            onBackTap = { navigator.navigateTo(ListDetailPaneScaffoldRole.Extra) },
                            viewModel = viewModel { RoomViewModel(it, messageRepository, roomRepository) }

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
