package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.Composable
import com.omasyo.gatherspace.domain.formatDateTime
import com.omasyo.gatherspace.models.response.UserSession
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
                onClick { onLogoutTap() }
            }
        ) {
            Text("Logout")
        }
    }
}