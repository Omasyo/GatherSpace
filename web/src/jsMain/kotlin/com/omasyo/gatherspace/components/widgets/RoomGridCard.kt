package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H3
import org.jetbrains.compose.web.dom.Text

object RoomGridCardStyle : StyleSheet() {
    init {
        id("room-grid-card") style {
//            display(DisplayStyle.Flex)
//            flexDirection(FlexDirection.Row)
//            alignItems(AlignItems.Center)
//            padding(8.px, 16.px)
        }
        type("h3") style {
            padding(4.px, 8.px, 0.px)
        }
    }
}

@Composable
fun RoomGridCard(
    room: Room
) {
    Style(RoomGridCardStyle)
    A(
        href = "/rooms/${room.id}",
    ) {
        Div(
            attrs = {
                id("room-grid-card")
                classes(MainStyle.customLink)
            }
        ) {
            Image(
                imageUrl = room.imageUrl,
                placeholder = "/image/room_placeholder.svg",
                attrs = {
                    style {
                        width(100.percent)
                    }
                }
            )
            H3 {
                Text(room.name)
            }
        }
    }
}