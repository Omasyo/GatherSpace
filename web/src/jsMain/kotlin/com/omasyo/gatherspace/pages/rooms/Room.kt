package com.omasyo.gatherspace.pages.rooms

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import app.cash.paging.compose.collectAsLazyPagingItems
import com.omasyo.gatherspace.common.domainComponent
import com.omasyo.gatherspace.common.homeViewModel
import com.omasyo.gatherspace.components.layouts.HomeLayout
import com.omasyo.gatherspace.components.sections.Header
import com.omasyo.gatherspace.components.sections.SideBar
import com.omasyo.gatherspace.components.widgets.room.RoomContent
import com.omasyo.gatherspace.home.HomeViewModel
import com.omasyo.gatherspace.room.RoomViewModel
import com.omasyo.gatherspace.room.RoomViewModelImpl
import com.varabyte.kobweb.core.Page
import com.varabyte.kobweb.core.rememberPageContext


@Page("{}")
@Composable
fun RoomPage() {
    val context = rememberPageContext()
    val roomId = context.route.params["room"]?.toInt()
    with(domainComponent) {
        RoomPage(
            roomViewModel = RoomViewModelImpl(roomId!!, messageRepository, roomRepository),
            homeViewModel = homeViewModel
        )
    }
}

@Composable
fun RoomPage(
    roomViewModel: RoomViewModel,
    homeViewModel: HomeViewModel,
) {
    HomeLayout(
        title = roomViewModel.room.collectAsState().value?.name ?: "",
        topBar = {
            Header()
        },
        sideBar = {
            SideBar()
        },
        isDisplayingContent = true,
    ) {
        RoomContent(
            onJoinTap = roomViewModel::joinRoom,
            isAuthenticated = homeViewModel.isAuthenticated.collectAsState().value,
            message = roomViewModel.message,
            onMessageChange = roomViewModel::changeMessage,
            onSendTap = roomViewModel::sendMessage,
            room = roomViewModel.room.collectAsState().value,
            oldMessages = roomViewModel.oldMessages.collectAsLazyPagingItems(),
            messages = roomViewModel.messages,
            onJoin = {
                homeViewModel.refresh()
            },
            onEventReceived = roomViewModel::onEventReceived,
            state = roomViewModel.state.collectAsState().value,
        )
    }
}

