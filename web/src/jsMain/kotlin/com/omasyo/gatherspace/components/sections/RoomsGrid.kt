package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.components.widgets.RoomGridCard
import com.omasyo.gatherspace.components.widgets.RoomGridCardPlaceholder
import com.omasyo.gatherspace.models.response.Room
import com.varabyte.kobweb.compose.css.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.css.overflowY
import org.jetbrains.compose.web.dom.Div

private object RoomsGridLayout : StyleSheet() {
    init {
        id("rooms-grid") style {
            overflowY(Overflow.Scroll)
            display(DisplayStyle.Grid)
            height(100.percent)
            property("grid-template-columns", " repeat(auto-fill,minmax(160px,1fr))")
            property("grid-auto-flow", "row")
            property("grid-auto-row", "minmax(160px,1fr)")
            gap(24.px)
            padding(20.px)
        }
    }
}

@Composable
fun RoomsGrid(
    state: UiState<List<Room>>
) {
    Style(RoomsGridLayout)
    when (state) {
        is UiState.Error -> {

        }

        UiState.Loading -> Div(attrs = {
            id("rooms-grid")
            style {
                overflowY(Overflow.Clip)
            }
        }) {
            repeat(40) {
                RoomGridCardPlaceholder()
            }
        }

        is UiState.Success -> {
            Div(attrs = {
                id("rooms-grid")
            }) {
                for (room in state.data) {
                    RoomGridCard(room)
                }
            }
        }
    }
}