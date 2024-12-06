package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantLight
import com.varabyte.kobweb.compose.css.background
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
                attrs = {
                    style {
                        width(48.px)
                        height(48.px)
                    }
                }
            )
            H3 {
                Text(room.name)
            }
        }
    }
}

@Composable
fun RoomListCardPlaceholder() {
    Style(RoomListCardStyle)
    Div(
        attrs = {
            id("room-list-card")
            classes(MainStyle.customLink)
        }
    ) {
        Div(
            attrs = {
                style {
                    width(48.px)
                    height(48.px)
                    background(lightDark(surfaceVariantLight, surfaceVariantDark))
                }
            }
        )
        Div(
            attrs = {
                style {
                    marginLeft(16.px)
                    width(144.px)
                    height(20.px)
                    background(lightDark(surfaceVariantLight, surfaceVariantDark))
                }
            }
        )
    }
}