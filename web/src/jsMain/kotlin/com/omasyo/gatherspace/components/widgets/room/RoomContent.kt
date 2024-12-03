package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paging.compose.LazyPagingItems
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.pages.date
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.borderTop
import com.varabyte.kobweb.compose.css.width
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

private object RoomStyle : StyleSheet() {
    init {
        id("room-wrapper") style {
            height(100.percent)
            width(100.percent)
            padding(56.px, 0.px)
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
    roomDetails: RoomDetails?,
    onRegisterTap: () -> Unit,
    onJoinTap: () -> Unit,
    isAuthenticated: Boolean,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendTap: () -> Unit,
    room: RoomDetails?,
    oldMessages: LazyPagingItems<Message>,
    messages: List<Message>,
    onJoin: () -> Unit,
//    state: RoomState
) {
    Style(RoomStyle)
    RoomTopBar(
        roomDetails = roomDetails
    )
    Div(
        attrs = {
            id("room-wrapper")
        }
    ) {
        Div(
            attrs = {
                id("messages-view")
            }
        ) {
            for (receivedMessage in messages) {
                Message(message = receivedMessage)
            }
            for(index in 0..<oldMessages.itemCount) {
                oldMessages[index]?.let { Message(message = it) }
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
//    LaunchedEffect(state) {
//        when (state.event) {
//            RoomEvent.JoinedRoom -> onJoin()
//            is RoomEvent.Error -> Unit
//            RoomEvent.MessageSent -> Unit
//            RoomEvent.None -> Unit
//        }
//    }
}