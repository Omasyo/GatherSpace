package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.*
import com.omasyo.gatherspace.styles.MainStyle
import com.omasyo.gatherspace.styles.lightDark
import com.omasyo.gatherspace.theme.surfaceVariantDark
import com.omasyo.gatherspace.theme.surfaceVariantLight
import com.varabyte.kobweb.browser.file.readBytes
import com.varabyte.kobweb.compose.css.*
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.graphics.Colors
import com.varabyte.kobweb.silk.components.icons.mdi.MdiClose
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import kotlinx.io.bytestring.encodeToByteString
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.mediacapture.MediaStream
import org.w3c.dom.mediacapture.MediaStreamConstraints
import org.w3c.files.File

object ImageCaptureStyle : StyleSheet() {
    val popup by style {
        position(Position.Fixed)
        zIndex(1)
        left(0.px)
        top(0.px)
        backgroundColor(Colors.Black.copyf(alpha = 0.5f))
        width(100.percent)
        height(100.percent)
        display(DisplayStyle.Flex)
    }

    val popupContent by style {
        display(DisplayStyle.Flex)
        flexDirection(FlexDirection.Column)
        backgroundColor(lightDark(surfaceVariantLight, surfaceVariantDark))
        maxWidth(512.px)
        width(90.percent)
        justifyItems(JustifyItems.Center)
        alignItems(org.jetbrains.compose.web.css.AlignItems.Center)
        padding(16.px)
        property("margin", "auto")
    }

    val imageStream by style {
        width(100.percent)
        gridArea("1/1")
        objectFit(ObjectFit.Cover)
    }

    val canvas by style {
        gridArea("1/1")
        objectFit(ObjectFit.Cover)
        display(DisplayStyle.None)
    }
}

@Composable
fun ImageCapture(
    onComplete: (Buffer) -> Unit,
    content: @Composable WebPhotoTakerScope.() -> Unit
) {
    Style(ImageCaptureStyle)
    val coroutineScope = rememberCoroutineScope()
    val webPhotoTakerScope by remember {
        mutableStateOf(WebPhotoTakerScope { file ->
            coroutineScope.launch {
                val buffer = Buffer()
                buffer.write(file.readBytes())
                onComplete(buffer)
            }
        })
    }
    webPhotoTakerScope.content()
    if (webPhotoTakerScope.isOpen) {
        Div(
            attrs = {
                classes(ImageCaptureStyle.popup)
            }
        ) {
            Div(
                attrs = {
                    classes(ImageCaptureStyle.popupContent)
                }
            ) {
                MdiClose(
                    modifier = Modifier.attrsModifier {
                        onClick { webPhotoTakerScope.clear() }
                        classes(MainStyle.clickable)
                        style {
                            alignSelf(com.varabyte.kobweb.compose.css.AlignSelf.End)
                            marginBottom(8.px)
                        }
                    }
                )
                Div(
                    attrs = {
                        style {
                            width(100.percent)
                            marginBottom(16.px)
                        }
                    }
                ) {
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
                                classes(ImageCaptureStyle.imageStream)
                            }
                        )
                        Canvas(attrs = {
                            id("canvas")
                            classes(ImageCaptureStyle.canvas)
                        })
                    }
                }
                Button(
                    attrs = {
                        id("start-button")
                    }
                ) {
                    Text("Take Picture")
                }
            }
        }
    }
    LaunchedEffect(webPhotoTakerScope.isOpen) {
        if (webPhotoTakerScope.isOpen) {
            webPhotoTakerScope.startup()
        }
    }
}

class WebPhotoTakerScope(
    private val onCapture: (File) -> Unit
) {
    var isOpen by mutableStateOf(false)

    private lateinit var video: HTMLVideoElement
    private lateinit var canvas: HTMLCanvasElement
    private lateinit var startButton: HTMLButtonElement

    private lateinit var mStream: MediaStream

    fun openCamera() {
        isOpen = true
    }

    private fun takePicture() {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D

        canvas.width = video.videoWidth
        canvas.height = (video.videoHeight * canvas.width) / video.videoWidth

        context.drawImage(video, 0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

        val data = canvas.toDataURL("image/png")
        data.encodeToByteString()
        canvas.toBlob({ blob ->
            val file = File(arrayOf(blob), "temp.png")
            onCapture(file)
            clear()
        })
    }

    fun clear() {
        isOpen = false
        mStream.getVideoTracks()[0].stop()
    }

    fun startup() {
        video = document.getElementById("video") as HTMLVideoElement
        canvas = document.getElementById("canvas") as HTMLCanvasElement
        startButton = document.getElementById("start-button") as HTMLButtonElement

        window.navigator.mediaDevices
            .getUserMedia(MediaStreamConstraints(video = true, audio = false))
            .then { stream ->
                mStream = stream
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
}