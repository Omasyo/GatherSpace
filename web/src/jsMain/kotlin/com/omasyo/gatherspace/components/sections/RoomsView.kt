package com.omasyo.gatherspace.components.sections

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.components.widgets.RoomListCard
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.H4
import org.jetbrains.compose.web.dom.Text

@Composable
fun RoomsView(
    rooms: List<Room>,
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .padding(topBottom = 8f.px)
            .overflow {
                y(Overflow.Scroll)
            },
    ) {
        A(
            href = "/join-room",
            attrs = {

                classes(MainStyle.customLink)
            }) {
            H4(
                attrs = {
                    style {
//                        top(0.px)
//                        position(Position.Sticky)
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
        for (room in rooms) {
            RoomListCard(room = room)
        }
    }
}