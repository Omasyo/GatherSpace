package com.omasyo.gatherspace.ui.components

import androidx.compose.runtime.*
import androidx.compose.ui.window.AwtWindow
import kotlinx.io.Buffer
import kotlinx.io.transferFrom
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.nio.file.Files

class DesktopChooserScope : ImageChooserScope {
    var isOpen by mutableStateOf(false)

    override fun chooseImage() {
        isOpen = true
    }

}

@Composable
actual fun ImageChooser(
    onComplete: (Buffer) -> Unit,
    content: @Composable ImageChooserScope.() -> Unit
) {

    val scope = remember { DesktopChooserScope() }

    scope.content()

    if (scope.isOpen) {
        FileDialog(
            onCloseRequest = { file ->
                scope.isOpen = false

                if (file == null) return@FileDialog

                val mime = Files.probeContentType(file.toPath())
                if (mime != null && mime.contains("image/")) {
                    onComplete(Buffer().transferFrom(file.inputStream()))
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