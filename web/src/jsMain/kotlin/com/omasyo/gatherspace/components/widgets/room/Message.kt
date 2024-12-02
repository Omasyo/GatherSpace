package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.components.widgets.Image
import com.omasyo.gatherspace.domain.formatTime
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceDark
import com.omasyo.gatherspace.theme.onSurfaceLight
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

private object MessageStyle : StyleSheet() {
    init {
        id("message") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            padding(8f.px, 16.px)
        }
        id("message-details") style {
            paddingLeft(8f.px)
        }
        id("message-content") style {}
        type("p") + firstChild style {
            marginTop(4f.px)
        }

    }
}

@Composable
fun Message(
    message: Message
) {
    Style(MessageStyle)
    Div(
        attrs = {
            id("message")
        }
    ) {
        Image(
            imageUrl = message.sender?.imageUrl,
            placeholder = "image/user_placeholder.svg",
            size = 40,
        )
        val lines = remember(message) {
            message.content.split("\n")
        }
        Div(
            attrs = {
                id("message-details")
            }
        ) {
            H4 {
                Span {
                    Text(message.sender?.username ?: "[deleted]")
                }
                Span(
                    attrs = {
                        style {
                            paddingLeft(4f.px)
                            color(lightDark(onSurfaceLight, onSurfaceDark))
                        }
                    }
                ) {
                    Text(message.created.formatTime())
                }
            }
            Div(
                attrs = {
                    id("message_content")
                }
            ) {
                for (line in lines) {
                    P {
                        Text(line)
                    }
                }
            }
        }
    }
}