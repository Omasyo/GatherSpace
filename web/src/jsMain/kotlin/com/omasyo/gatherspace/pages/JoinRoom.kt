package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.RoomsGrid
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.viewmodels.homeViewModel
import com.varabyte.kobweb.core.Page


@Page
@Composable
fun JoinRoomPage(
) {
    HomeLayout(
        title = "GatherSpace",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = true,
    ) {
        RoomsGrid(
            state = homeViewModel.allRooms.collectAsState().value,
        )
    }
}