package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.styles.MainStyle
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.margin
import com.varabyte.kobweb.compose.ui.toAttrs
import com.varabyte.kobweb.silk.components.icons.mdi.MdiArrowBack
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.A
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text

private object RoomTopBarStyle : StyleSheet() {
    init {
        id("room-topBar") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Center)
            padding(0.px, 8.px)
            height(56.px)
        }
    }
}

@Composable
fun RoomTopBar(
    roomDetails: RoomDetails?,
) {
    Style(RoomTopBarStyle)
    Div(
        attrs = {
            id("room-topBar")
        }
    ) {
        MdiArrowBack(Modifier.attrsModifier {
            onClick { window.location.href = "/" }
            classes(MainStyle.clickable)
        })
        if (roomDetails != null) {
            Box(
                modifier = Modifier.attrsModifier {
                    classes(MainStyle.customLink)
                }
            ) {
                A(
                    href = "/rooms/${roomDetails.id}",
                ) {
                    H1(
                        attrs = Modifier.margin { left(8.px) }.toAttrs()
                    ) {
                        Text(roomDetails.name)
                    }
                }
            }
        }
    }
}