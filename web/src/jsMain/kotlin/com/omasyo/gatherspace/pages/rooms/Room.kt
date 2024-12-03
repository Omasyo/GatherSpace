package com.omasyo.gatherspace.pages.rooms

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.widgets.room.RoomContent
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.pages.date
import com.varabyte.kobweb.core.Page
import kotlinx.coroutines.flow.MutableStateFlow

@Page("{}")
@Composable
fun RoomPage() {

    HomeLayout(
        title = "Room Name",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = true,
//        showSideBar = false,
    ) {
        RoomContent(
            roomDetails = RoomDetails(
                id = 1268,
                name = "Pearl Bender",
                imageUrl = null,
                creator = null,
                isMember = false,
                members = listOf(),
                created = date,
                modified = date
            ),

            onRegisterTap = {},
            onJoinTap = {},
            isAuthenticated = false,
            message = "pharetra",
            onMessageChange = {},
            onSendTap = {},
            room = null,
            oldMessages = fakeDataFlow.collectAsLazyPagingItems(),
            messages = listOf(),
            onJoin = {},
        )
    }
}


val fakeData = List(10) {
    Message(
        id = it + 3, content = "molestie", sender = User(
            id = 9558,
            username = "Marietta Lyons",
            imageUrl = null
        ), roomId = 2479, created = date, modified = date
    )
}

val pagingData = PagingData.from(fakeData)

val fakeDataFlow = MutableStateFlow(pagingData)