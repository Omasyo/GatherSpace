package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.AwtWindow
import com.github.sarxos.webcam.Webcam
import com.omasyo.gatherspace.createroom.BottomSheetMenuItem
import kotlinx.io.Buffer
import kotlinx.io.transferFrom
import java.awt.BorderLayout
import java.awt.Font
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JPanel
import javax.swing.border.EmptyBorder


class DesktopPhotoTakerScope : PhotoTakerScope {
    var isOpen by mutableStateOf(false)

    override fun takePhoto() {
        isOpen = true
    }

}

@Composable
actual fun PhotoTaker(
    modifier: Modifier,
    onComplete: (Buffer) -> Unit,
    content: @Composable PhotoTakerScope.() -> Unit
) {
    val scope = remember { DesktopPhotoTakerScope() }
    BottomSheetMenuItem(
        text = "Take a picture",
        onTap = {
            scope.isOpen = true
        },
    )

    if (scope.isOpen) {
        WebcamPanel(
            onCloseRequest = { file ->
                scope.isOpen = false
                if (file != null) {
                    onComplete(Buffer().transferFrom(file.inputStream()))
                }
            },
        )
    }
}

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

            val webcamPanel = com.github.sarxos.webcam.WebcamPanel(webcam).apply {
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