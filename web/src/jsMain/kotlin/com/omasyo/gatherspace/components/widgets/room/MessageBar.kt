package com.omasyo.gatherspace.components.widgets.room

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.onSurfaceVariantDark
import com.omasyo.gatherspace.theme.onSurfaceVariantLight
import com.omasyo.gatherspace.theme.primaryDark
import com.omasyo.gatherspace.theme.primaryLight
import com.varabyte.kobweb.compose.css.TextDecorationLine
import com.varabyte.kobweb.compose.css.borderBottom
import com.varabyte.kobweb.compose.css.color
import com.varabyte.kobweb.compose.css.textDecorationLine
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.onClick
import com.varabyte.kobweb.compose.ui.modifiers.padding
import com.varabyte.kobweb.silk.components.icons.mdi.MdiSend
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

private object MessageBarStyle : StyleSheet() {
    init {
        id("message-bar") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Center)
            padding(0.px, 8.px)
            height(56.px)
        }
        type("input") style {
            padding(0.px, 8.px)
        }
        id("message-bar-placeholder") style {
            display(DisplayStyle.Flex)
            flexDirection(FlexDirection.Row)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            padding(0.px, 8.px)
            width(100.percent)
            height(56.px)
        }
        id("action") style {
            color(lightDark(primaryLight, primaryDark))
        }
        id("action") + hover style {
            textDecorationLine(TextDecorationLine.Underline)
            property("cursor", "pointer")
        }
    }
}

@Composable
fun MessageBar(
    message: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit
) {
    Style(MessageBarStyle)
    Div(
        attrs = { id("message-bar") },
    ) {
        TextInput(message) {
            onInput {
                onMessageChange(it.value)
            }
        }
        MdiSend(Modifier.onClick {
            onSendClick()
        }.attrsModifier {
            classes(MainStyle.clickable)
        })
    }
}

@Composable
fun MessageFieldPlaceholder(
    message: String,
    actionText: String,
    action: () -> Unit
) {
    Style(MessageBarStyle)
    Div(
        attrs = { id("message-bar-placeholder") },
    ) {
        H4 {
            Text("$message ")
            Span(
                attrs = {
                    id("action")
                    onClick { action() }
                }
            ) {
                Text(actionText)
            }
        }
    }
}


@Composable
fun MessageFieldPlaceholder(
    message: String,
    actionText: String,
    href: String
) {
    Style(MessageBarStyle)
    Div(
        attrs = { id("message-bar-placeholder") },
    ) {
        H4 {
            Text("$message ")
            A(
                href = href,
            ) {
                Span(
                    attrs = {
                        id("action")
                    }
                ) {
                    Text(actionText)
                }
            }
        }
    }
}
