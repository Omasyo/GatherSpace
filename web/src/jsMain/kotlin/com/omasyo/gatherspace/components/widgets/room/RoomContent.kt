package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.pages.date
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.varabyte.kobweb.compose.css.Overflow
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.borderTop
import com.varabyte.kobweb.compose.css.width
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxSize
import com.varabyte.kobweb.compose.ui.modifiers.fillMaxWidth
import com.varabyte.kobweb.compose.ui.modifiers.height
import com.varabyte.kobweb.compose.ui.modifiers.overflow
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.P
import org.jetbrains.compose.web.dom.Text

private object RoomStyle : StyleSheet() {
    init {
        id("room-wrapper") style {
            height(100.percent)
            width(100.percent)
            padding(56.px, 0.px)
        }
        id("messages-view") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Column)
            overflowY("scroll")
            width(100.percent)
            height(100.percent)
            borderTop {
                width(1.px)
                style(LineStyle.Solid)
                color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
            }
            borderBottom {
                width(1.px)
                style(LineStyle.Solid)
                color(lightDark(onSurfaceVariantDark, onSurfaceVariantLight))
            }
        }
    }
}

@Composable
fun RoomContent(
    roomDetails: RoomDetails?
) {
    Style(RoomStyle)
    RoomTopBar(
        roomDetails = roomDetails
    )
    Div(
        attrs = {
            id("room-wrapper")
        }
    ) {
        Div(
            attrs = {
                id("messages-view")
            }
        ) {
            repeat(500) {
                Message(
                    message = Message(
                        id = 1113,
                        content = "Styling is an essential part of your app development. With that, you can improve the user experience and make your app look better.\n" +
                                "\n" +
                                "For Web applications, the styling part is mainly done with CSS. CSS stands for Cascading Style Sheets, a language used to describe the presentation of a document. As Compose for Web still uses HTML for rendering the components, we will use CSS as the styling language.",
                        sender = null,
                        roomId = 2398,
                        created = date,
                        modified = date
                    )
                )
            }
        }
        if (false) {
            MessageBar(
                message = "",
                onMessageChange = {},
                onSendClick = {}
            )
        } else {
            MessageFieldPlaceholder(
                message = "This is an Error",
                actionText = "Retry",
                action = {},
            )
        }
    }
}