package com.omasyo.gatherspace.pages

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.RoomsGrid
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.models.response.Room
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
            rooms = List(54) {
                Room(id = 2737, name = "Kim Merritt", imageUrl = null)
            }
        )
    }
}