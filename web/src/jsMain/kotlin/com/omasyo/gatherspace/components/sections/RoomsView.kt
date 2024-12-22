package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.components.widgets.RoomListCard
import com.omasyo.gatherspace.components.widgets.RoomListCardPlaceholder
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxHeight
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import com.varabyte.kobweb.compose.ui.modifiers.padding
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun RoomsView(
    state: UiState<List<Room>>
) {
    when (state) {
        is UiState.Error -> {
            JoinRoomButton()
        }

        UiState.Loading -> Placeholder()
        is UiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(topBottom = 8f.px)
                    .overflow {
                        y(Overflow.Scroll)
                    },
            ) {
                JoinRoomButton()
                for (room in state.data) {
                    RoomListCard(room = room)
                }
            }
        }
    }
}

@Composable
private fun JoinRoomButton() {
    A(
        href = "/join-room",
        attrs = {

            classes(MainStyle.customLink)
        }) {
        H4(
            attrs = {
                style {
                    width(100.percent)
                    textAlign("center")
                    padding(8.px, 0.px)
                    borderBottom {
                        width(1.px)
                        style(LineStyle.Solid)
                        color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
                    }
                }
            }
        ) {
            Text("Join A Room")
        }
    }
}

@Composable
private fun Placeholder() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(topBottom = 8f.px)
            .overflow {
                y(Overflow.Clip)
            },
    ) {
        repeat(20) {
            RoomListCardPlaceholder()
        }
    }
}