package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.components.widgets.RoomGridCard
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
//            flexDirection(FlexDirection.Column)
//            flexWrap(FlexWrap.Wrap)
            height(100.percent)
            property("grid-template-columns", " repeat(auto-fill,minmax(160px,1fr))")
            property("grid-auto-flow", "row")
            property("grid-auto-row", "minmax(160px,1fr)")
            gap(24.px)
            padding(20.px)
        }
//        media(mediaMinWidth(400.px) and mediaMaxWidth(800.px)) {
//            id("rooms-grid") style {
//                property("grid-template-columns", "repeat(3, 1fr)")
//            }
//        }
//        media(mediaMinWidth(800.px) and mediaMaxWidth(1000.px)) {
//            id("rooms-grid") style {
//                property("grid-template-columns", "repeat(4, 1fr)")
//            }
//        }
//        media(mediaMinWidth(800.px) and mediaMaxWidth(1600.px)) {
//            id("rooms-grid") style {
//                property("grid-template-columns", "repeat(5, 1fr)")
//            }
//        }
    }
}

@Composable
fun RoomsGrid(
    rooms: List<Room>,
) {
    Style(RoomsGridLayout)
    Div(attrs = {
        id("rooms-grid")
    }) {
        for (room in rooms) {
            RoomGridCard(room)
        }
    }
}