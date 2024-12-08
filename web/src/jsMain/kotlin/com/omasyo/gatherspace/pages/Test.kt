package com.omasyo.gatherspace.pages

import androidx.compose.runtime.*
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.core.Page
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.io.Buffer
import kotlinx.io.writeString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.*
import org.w3c.dom.mediacapture.MediaStreamConstraints

@Page
@Composable
fun Test(
    width: Int = 600,
) {
    var toss by remember { mutableStateOf<Buffer?>(null) }
    Div(
        attrs = {
            style {
                display(DisplayStyle.Grid)
            }
        }
    ) {
        Video(
            attrs = {
                id("video")
                width(width)
                style {
                    backgroundColor(Color.red)
                    gridArea("1/1")
                    objectFit(ObjectFit.Cover)
                }
            }
        )
        Canvas(attrs = {
            id("canvas")
            style {
                backgroundColor(Color.green)
                gridArea("1/1")
                objectFit(ObjectFit.Cover)
                if (toss == null) {
                    visibility(Visibility.Hidden)
                }
            }
        })
    }
    Button(
        attrs = {
            id("startbutton")
        }
    ) {
        Text("Button")
    }
    Button(
        attrs = {
            onClick {
                toss = null
            }
        }
    ) {
        Text("Reset")
    }

    LaunchedEffect(Unit) {
        lateinit var video: HTMLVideoElement
        lateinit var canvas: HTMLCanvasElement
        lateinit var startButton: HTMLButtonElement

        fun takePicture() {
            val context = canvas.getContext("2d") as CanvasRenderingContext2D

            canvas.width = width
            canvas.height = (video.videoHeight * width) / video.videoWidth

            context.drawImage(video, 0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

            val data = canvas.toDataURL("image/png")
            println("Got data $data")
            val buffer = Buffer()
            buffer.writeString(data)
            println(buffer)
            toss = buffer
        }

        fun startup() {
            video = document.getElementById("video") as HTMLVideoElement
            canvas = document.getElementById("canvas") as HTMLCanvasElement
            startButton = document.getElementById("startbutton") as HTMLButtonElement

            window.navigator.mediaDevices
                .getUserMedia(MediaStreamConstraints(video = true, audio = false))
                .then { stream ->
                    video.srcObject = stream
                    video.play()
                }
                .catch {
                    console.error("An Error occurred: $it")
                }

            startButton.addEventListener("click", {
                takePicture()
                it.preventDefault()
            }, false)
        }

        window.addEventListener("load", {
            startup()
        }, false)
    }
}