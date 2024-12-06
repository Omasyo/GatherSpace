package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.viewmodels.homeViewModel

@Composable
fun SideBar() {
    RoomsView(
        state = homeViewModel.userRooms.collectAsState().value
    )
}