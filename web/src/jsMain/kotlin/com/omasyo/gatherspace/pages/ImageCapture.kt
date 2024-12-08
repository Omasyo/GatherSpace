package com.omasyo.gatherspace.pages

import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.io.Buffer
import kotlinx.io.writeString
import org.w3c.dom.*
import org.w3c.dom.mediacapture.MediaStreamConstraints

//import web.media.recorder.MediaRecorder

class ImageCapture {
    var width: Int = 320
    var height: Int = 320

    var streaming = false

    lateinit var video: HTMLVideoElement
    lateinit var canvas: HTMLCanvasElement
    lateinit var startButton: HTMLButtonElement

    private fun clearPhoto() {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        context.fillStyle = "#AAA"
        context.fillRect(0.0, 0.0, canvas.width.toDouble(), canvas.height.toDouble())

        val data = canvas.toDataURL("image/png")
//        photo.setAttribute("src", data)
    }

    fun takePicture() {
        val context = canvas.getContext("2d") as CanvasRenderingContext2D
        if (width != 0 && height != 0) {
            canvas.width = width
            canvas.height = height
            context.drawImage(video, 0.0, 0.0, width.toDouble(), height.toDouble())

            val data = canvas.toDataURL("image/png")
            println("Got data $data")
            val buffer = Buffer()
            buffer.writeString(data)
            println(buffer)
        } else {
            clearPhoto()
        }
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

        video.addEventListener("canplay", {
            if (!streaming) {
                height = video.videoHeight / (video.videoWidth / width)

                video.width = width
                video.height = height
                canvas.width = width
                canvas.height = height
                streaming = true
            }
        }, false)

        startButton.addEventListener("click", {
            takePicture()
            it.preventDefault()
        }, false)
        clearPhoto()
    }


    init {
        window.addEventListener("load", {
            startup()
        }, false)
    }
}