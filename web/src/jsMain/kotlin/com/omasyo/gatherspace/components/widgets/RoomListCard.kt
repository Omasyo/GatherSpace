package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

object RoomListCardStyle : StyleSheet() {
    init {
        id("room-list-card") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Center)
            padding(8.px, 16.px)
        }
        type("h3") style {
            padding(0.px, 8.px)
        }
    }
}

@Composable
fun RoomListCard(
    room: Room
) {
    Style(RoomListCardStyle)
    A(
        href = "/rooms/${room.id}",
    ) {
        Div(
            attrs = {
                id("room-list-card")
                classes(MainStyle.customLink)
            }
        ) {
            Image(
                imageUrl = room.imageUrl,
                placeholder = "/image/room_placeholder.svg",
                size = 48
            )
            H3 {
                Text(room.name)
            }
        }
    }
}