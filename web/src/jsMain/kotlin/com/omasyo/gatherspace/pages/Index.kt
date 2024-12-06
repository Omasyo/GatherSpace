package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.sections.RoomsGrid
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.createroom.CreateRoomEvent
import com.omasyo.gatherspace.home.HomeViewModel
import com.omasyo.gatherspace.home.HomeViewModelImpl
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.viewmodels.homeViewModel
import com.varabyte.kobweb.core.Page
import kotlinx.datetime.LocalDateTime

val date = LocalDateTime(1, 1, 1, 1, 1)

@Page
@Composable
fun HomePage(
) {
    HomeLayout(
        title = "GatherSpace",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = false,
    ) {
        RoomsGrid(
            state = homeViewModel.allRooms.collectAsState().value,
        )
    }
}