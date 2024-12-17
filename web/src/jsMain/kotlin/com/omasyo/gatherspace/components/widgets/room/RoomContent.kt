package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paging.compose.LazyPagingItems
import com.omasyo.gatherspace.components.layouts.showSnackbar
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.room.RoomEvent
import com.omasyo.gatherspace.room.RoomState
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.borderTop
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div

private object RoomStyle : StyleSheet() {
    init {
        id("room-wrapper") style {
            height(100.percent)
            width(100.percent)
            paddingBottom(112.px)
            maxWidth(1200.px)
            position(Position.Relative)
        }
        id("messages-view") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.ColumnReverse)
            overflowY("scroll")
            width(100.percent)
            height(100.percent)
            borderTop {
                width(1.px)
                style(LineStyle.Solid)
                color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
            }
            borderBottom {
                width(1.px)
                style(LineStyle.Solid)
                color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
            }
        }
    }
}

@Composable
fun RoomContent(
    onJoinTap: () -> Unit,
    isAuthenticated: Boolean,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendTap: () -> Unit,
    room: RoomDetails?,
    oldMessages: LazyPagingItems<Message>,
    messages: List<Message>,
    onJoin: () -> Unit,
    onEventReceived: (RoomEvent) -> Unit,
    state: RoomState
) {
    Style(RoomStyle)
    Div(
        attrs = {
            id("room-wrapper")
        }
    ) {
        RoomTopBar(
            roomDetails = room
        )
        Div(
            attrs = {
                id("messages-view")
            }
        ) {
            for (receivedMessage in messages.asReversed()) {
                Message(message = receivedMessage)
            }
            for (index in 0..<oldMessages.itemCount) {
                oldMessages[index]?.let { Message(message = it) } // TODO: Loads the full data, you know why - Find solution
            }
        }
        when {
            !isAuthenticated -> {
                MessageFieldPlaceholder("Share your thoughts", actionText = "Login", href = "/login")
            }

            !(room?.isMember ?: false) -> {
                MessageFieldPlaceholder("Not a member of this room", actionText = "Join?", action = onJoinTap)
            }

            else -> {
                MessageBar(
                    message = message,
                    onMessageChange = onMessageChange,
                    onSendClick = onSendTap
                )
            }
        }
    }

    LaunchedEffect(state.event) {
        when (val event = state.event) {

            RoomEvent.JoinedRoom -> onJoin()
            is RoomEvent.Error -> {
                showSnackbar(event.message)
            }

            RoomEvent.MessageReceived -> Unit

            else -> Unit
        }
        onEventReceived(state.event)
    }
}