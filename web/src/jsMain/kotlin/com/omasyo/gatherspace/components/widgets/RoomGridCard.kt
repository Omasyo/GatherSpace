package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.primaryContainerDark
import com.omasyo.gatherspace.theme.primaryContainerLight
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantLight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.aspectRatio
import com.varabyte.kobweb.compose.css.background
import com.varabyte.kobweb.compose.css.overflow
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
                        aspectRatio(1)
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
fun RoomGridCardPlaceholder() {
    Style(RoomGridCardStyle)
    Div(
        attrs = {
            id("room-grid-card")
            classes(MainStyle.customLink)
        }
    ) {
        Div(
            attrs = {
                style {
                    width(100.percent)
                    aspectRatio(1)
                    background(lightDark(surfaceVariantLight, surfaceVariantDark))
                }
            }
        )
        Div(
            attrs = {
                style {
                    marginTop(16.px)
                    width(70.percent)
                    height(24.px)
                    background(lightDark(surfaceVariantLight, surfaceVariantDark))
                }
            }
        )
    }
}