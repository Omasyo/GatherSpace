package com.omasyo.gatherspace.createroom

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.AwtWindow
import com.github.sarxos.webcam.Webcam
import com.github.sarxos.webcam.WebcamPanel
import kotlinx.io.Buffer
import kotlinx.io.transferFrom
import java.awt.*
import java.io.File
import java.nio.file.Files
import javax.imageio.ImageIO
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.border.EmptyBorder


@Composable
actual fun TakePictureItem(modifier: Modifier, onComplete: (Buffer) -> Unit) {

    var isOpen by remember { mutableStateOf(false) }
    BottomSheetMenuItem(
        text = "Take a picture",
        onTap = {
            isOpen = true
        },
    )

    if (isOpen) {
        WebcamPanel(
            onCloseRequest = { file ->
                isOpen = false
                if (file != null) {
                    onComplete(Buffer().transferFrom(file.inputStream()))
                }
            },
        )
    }
}

@Composable
actual fun ChoosePictureItem(modifier: Modifier, onComplete: (Buffer) -> Unit) {
    var isOpen by remember { mutableStateOf(false) }

    BottomSheetMenuItem(
        text = "Choose from gallery",
        onTap = { isOpen = true }
    )
    if (isOpen) {
        FileDialog(
            onCloseRequest = { file ->
                isOpen = false

                if (file == null) return@FileDialog
                println("Mime type ${Files.probeContentType(file.toPath())}")

                val mime = Files.probeContentType(file.toPath())
                if (mime != null && mime.contains("image/")) {
                    onComplete(Buffer().transferFrom(file.inputStream()))
                    println("Result ${file.absolutePath}")
                }
            }
        )
    }
}

@Composable
private fun FileDialog(
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog((null as Frame?), "Choose a file", LOAD) {
            init {
                isMultipleMode = false
            }

            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (value) {
                    onCloseRequest(files.firstOrNull())
                }
            }
        }
    },
    dispose = FileDialog::dispose
)

@Composable
private fun WebcamPanel(
    onCloseRequest: (result: File?) -> Unit
) = AwtWindow(
    create = {
        object : JDialog() {

            var image: File? = null
            val webcam = Webcam.getDefault().apply {
                viewSize = viewSizes.last()
            }

            val webcamPanel = WebcamPanel(webcam).apply {
                isFPSDisplayed = true
                isDisplayDebugInfo = true
                isImageSizeDisplayed = true
                isMirrored = true
            }

            val captureButton = JButton("Capture").apply {
                setBorder(EmptyBorder(16, 16, 16, 16))
                font = Font(null, Font.PLAIN, 20)
                addActionListener {
                    image = File.createTempFile("webcam", "png")
                    val bufferedImage = webcam.image
                    ImageIO.write(bufferedImage, "png", image)

                    dispose()
                }
            }

            init {
                val panel = JPanel()
                panel.layout = BorderLayout()


                panel.add(webcamPanel, BorderLayout.CENTER)
                panel.add(captureButton, BorderLayout.SOUTH)

                add(panel)

                isResizable = true
                defaultCloseOperation = DISPOSE_ON_CLOSE
                pack()
            }

            override fun dispose() {
                super.dispose()

                webcam.close()
                Webcam.getDiscoveryService().stop()
                onCloseRequest(image)
            }
        }
    },
    dispose = JDialog::dispose
)