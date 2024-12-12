package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.components.widgets.Image
import com.omasyo.gatherspace.domain.formatTime
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.MainStyle.style
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceDark
import com.omasyo.gatherspace.theme.onSurfaceLight
import com.varabyte.kobweb.compose.css.FontWeight
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.css.fontWeight
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
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
            placeholder = "/image/user_placeholder.svg",
            attrs = {
                style {
                    width(40.px)
                    height(40.px)
                }
            }
        )
        Div(
            attrs = {
                id("message-details")
            }
        ) {
            H3(
                attrs = {
                    style {
                        padding(0.px)
                    }
                }
            ) {
                A(
                    href = message.sender?.id?.let { "/users/$it" },
                    attrs = { classes(MainStyle.customLink) }
                ) {
                    Span {
                        Text(message.sender?.username ?: "[deleted]")
                    }
                }
                Span(
                    attrs = {
                        style {
                            fontSize(0.9.cssRem)
                            fontWeight(FontWeight.Normal)
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
                val lines = remember(message) {
                    message.content.split("\n")
                }
                for (line in lines) {
                    P {
                        Text(line)
                    }
                }
            }
        }
    }
}