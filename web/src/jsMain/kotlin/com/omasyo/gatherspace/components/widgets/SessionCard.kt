package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.domain.formatDateTime
import com.omasyo.gatherspace.models.response.UserSession
import com.varabyte.kobweb.compose.css.JustifyItems
import com.varabyte.kobweb.compose.css.JustifySelf
import com.varabyte.kobweb.compose.css.justifyItems
import com.varabyte.kobweb.compose.css.justifySelf
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun SessionCard(
    sessionDetails: UserSession,
    onLogoutTap: () -> Unit,
) {
    Div(
        attrs = {
            style {
                display(DisplayStyle.Flex)
                alignItems(AlignItems.Center)
                justifyContent(JustifyContent.SpaceBetween)
                width(100.percent)
            }
        }
    ) {
        Div {
            H3 {
                Text(sessionDetails.deviceName)
            }
            P {
                Text(
                    "Recently accessed: ${sessionDetails.lastAccessed.formatDateTime()}"
                )
            }
        }
        Button(
            attrs = {
                style {
                }
            }
        ) {
            Text("Logout")
        }
    }
}