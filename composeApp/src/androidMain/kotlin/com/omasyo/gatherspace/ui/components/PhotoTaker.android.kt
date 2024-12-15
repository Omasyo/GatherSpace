package com.omasyo.gatherspace.ui.components

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.omasyo.gatherspace.createroom.BottomSheetMenuItem
import kotlinx.io.Buffer
import kotlinx.io.transferFrom
import java.io.File


class AndroidPhotoTakerScope(
    private val cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    private val fileUri: Uri
) : PhotoTakerScope {
    override fun takePhoto() {
        cameraLauncher.launch(fileUri)
    }
}

@Composable
actual fun PhotoTaker(
    onComplete: (Buffer) -> Unit,
    content: @Composable PhotoTakerScope.() -> Unit
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {

            val inputStream =
                fileUri?.let { context.contentResolver.openInputStream(it) } ?: return@rememberLauncherForActivityResult
            val buffer = Buffer().transferFrom(inputStream)
            onComplete(buffer)
        }
    }
    val scope = remember {
        if (fileUri == null) {
            fileUri = createTempFile(context)
        }
        AndroidPhotoTakerScope(launcher, fileUri!!)
    }

    scope.content()
}

private var fileUri: Uri? = null

private fun createTempFile(context: Context): Uri {
    val file = File(context.cacheDir, "temp.jpg")
    println(file.absolutePath)

    return FileProvider.getUriForFile(context, context.packageName + ".provider", file)
}