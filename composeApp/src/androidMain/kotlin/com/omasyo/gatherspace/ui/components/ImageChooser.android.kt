package com.omasyo.gatherspace.ui.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.omasyo.gatherspace.createroom.BottomSheetMenuItem
import io.github.aakira.napier.Napier
import kotlinx.io.Buffer
import kotlinx.io.transferFrom


class AndroidChooserScope(
    private val pickMedia: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>
) : ImageChooserScope {


    override fun chooseImage() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}

@Composable
actual fun ImageChooser(
    onComplete: (Buffer) -> Unit,
    content: @Composable ImageChooserScope.() -> Unit
) {
    val context = LocalContext.current
    val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        Napier.i(tag = "CreateRoomScreen.android") { "TakePictureItem: $uri" }
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return@let
            val buffer = Buffer().transferFrom(inputStream)
            onComplete(buffer)
            inputStream.close()
        }
    }

    val scope = remember { AndroidChooserScope(pickMedia) }
    scope.content()
}