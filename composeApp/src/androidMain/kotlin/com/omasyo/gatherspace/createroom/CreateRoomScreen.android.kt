package com.omasyo.gatherspace.createroom

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.core.net.toUri
import io.github.aakira.napier.Napier
import kotlinx.io.Buffer
import kotlinx.io.files.FileSystem
import kotlinx.io.transferFrom
import okio.Okio
import java.io.File


private var fileUri: Uri? = null

private fun createTempFile(context: Context): Uri {
    val file = File(context.cacheDir, "temp.jpg")
    println(file.absolutePath)

    return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
}

@Composable
actual fun TakePictureItem(modifier: Modifier, onComplete: (Buffer) -> Unit) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            onComplete(Buffer().transferFrom(fileUri!!.toFile().inputStream()))
        }
    }

    val context = LocalContext.current
    BottomSheetMenuItem(
        text = "Take a picture",
        onTap = {
            if (fileUri == null) {
                fileUri = createTempFile(context)
            }
            launcher.launch(fileUri!!)
        }
    )
}

@Composable
actual fun ChoosePictureItem(modifier: Modifier, onComplete: (Buffer) -> Unit) {
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


    BottomSheetMenuItem(
        text = "Choose from gallery",
        onTap = {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    )
}