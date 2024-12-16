package com.omasyo.gatherspace.components.widgets

import androidx.compose.runtime.*
import com.varabyte.kobweb.browser.file.readBytes
import kotlinx.browser.document
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.io.Buffer
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.asList
import org.w3c.files.File


class WebImageChooserScope(
    private val onSelect: (File) -> Unit
) {
    fun chooseImage() {
        val input = document.createElement("input") as HTMLInputElement
        input.type = "file"
        input.accept = "image/*"

        input.onchange = {
            (it.target as HTMLInputElement).files?.asList()?.get(0)?.let {
                onSelect(it)
            }
        }

        input.click()
    }

}

@Composable
fun ImageChooser(
    onComplete: (Buffer) -> Unit,
    content: @Composable WebImageChooserScope.() -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val imageChooserScope = remember {
        WebImageChooserScope {
            coroutineScope.launch {
                val buffer = Buffer()
                buffer.write(it.readBytes())
                onComplete(buffer)
            }
        }
    }

    imageChooserScope.content()
}