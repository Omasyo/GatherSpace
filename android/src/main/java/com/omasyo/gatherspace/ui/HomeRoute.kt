package com.omasyo.gatherspace.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.HomeScreen
import com.omasyo.gatherspace.HomeViewModel
import com.omasyo.gatherspace.domain.room.RoomRepositoryImpl
import com.omasyo.gatherspace.network.room.createNetworkSource
import kotlinx.coroutines.Dispatchers

val repository = RoomRepositoryImpl(createNetworkSource(), Dispatchers.IO)

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    onRoomTap: (Int) -> Unit,
    onCreateRoom: () -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel(repository) }
) {
    HomeScreen(
        modifier = modifier,
        onRoomTap = onRoomTap,
        onCreateRoomTap = onCreateRoom,
        state = viewModel.rooms.collectAsState().value
    )
}