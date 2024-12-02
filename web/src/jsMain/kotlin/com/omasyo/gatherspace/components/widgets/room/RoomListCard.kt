package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.components.widgets.Image
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.LayoutStyle
import com.omasyo.gatherspace.styles.MainStyle
import com.varabyte.kobweb.compose.foundation.layout.Arrangement
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.compose.ui.modifiers.size
import com.varabyte.kobweb.compose.ui.toAttrs
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
    modifier: Modifier = Modifier,
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
                placeholder = "image/room_placeholder.svg",
                size = 48
            )
            H3 {
                Text(room.name)
            }
        }
    }
}