package org.example.app.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import com.omasyo.gatherspace.models.response.Room
import com.varabyte.kobweb.compose.foundation.layout.Box
import com.varabyte.kobweb.compose.foundation.layout.Column
import com.varabyte.kobweb.compose.foundation.layout.Row
import com.varabyte.kobweb.compose.ui.Alignment
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.compose.ui.modifiers.*
import com.varabyte.kobweb.core.Page
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.attributes.alt
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*
import org.w3c.dom.mediacapture.MediaStreamConstraints


@Page
@Composable
fun HomePage(
) {

    Div {
        H1 {
            Text("This is My Example")
        }
        Div(attrs = {
            classes("camera")
        }) {
            Video(attrs = {
                id("video")
            }) {
                Text("Video stream not available")
            }
            Button(attrs = {
                id("startbutton")
            }) {
                Text("Take photo")
            }
        }
        Canvas(attrs = {
            id("canvas")
        })
        Div(attrs = {
            classes("output")
        }) {
            Img(attrs = {
                id("photo")
            }, alt = "The screen caputre will appear herea", src = "")
            Video(attrs = {
                id("stream")
            }) {
                Text(" stream not available")
            }
        }
    }

    LaunchedEffect(Unit) {

        ImageCapture()
    }
}
