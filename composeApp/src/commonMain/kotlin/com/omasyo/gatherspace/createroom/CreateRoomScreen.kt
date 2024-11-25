package com.omasyo.gatherspace.createroom

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.ui.components.TextFieldState
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import kotlinx.io.Buffer
import kotlinx.io.snapshot

@Composable
fun CreateRoomRoute(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    onRoomCreated: (Int) -> Unit,
    onAuthError: () -> Unit,
    viewModel: CreateRoomViewModel = dependencyProvider {
        viewModel { CreateRoomViewModel(roomRepository) }
    },
) {
    CreateRoomScreen(
        modifier = modifier,
        onBackTap = onBackTap,
        roomName = viewModel.nameField,
        onRoomNameChange = viewModel::changeName,
        description = viewModel.descriptionField,
        onDescriptionChange = viewModel::changeDescription,
        image = viewModel.image,
        setImage = viewModel::updateImage,
        onSubmit = viewModel::submit,
        onEventReceived = { event ->
            if (event is CreateRoomEvent.Success) {
                onRoomCreated(event.id)
            }
            viewModel.onEventReceived(event)
        },
        onAuthError = onAuthError,
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateRoomScreen(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    roomName: TextFieldState,
    onRoomNameChange: (String) -> Unit,
    description: TextFieldState,
    onDescriptionChange: (String) -> Unit,
    image: Buffer?,
    setImage: (Buffer) -> Unit,
    onSubmit: () -> Unit,
    onEventReceived: (CreateRoomEvent) -> Unit,
    onAuthError: () -> Unit,
    state: CreateRoomState
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold { innerPadding ->

        IconButton(
            onClick = onBackTap,
            modifier = Modifier.padding(innerPadding).padding(top = 16f.dp, start = 8f.dp)
        ) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = null)
        }
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16f.dp, vertical = 72f.dp),
            verticalArrangement = Arrangement.spacedBy(8f.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Create Room",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Column {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8f.dp),
                ) {
                    CreateRoomTextField(
                        state = roomName,
                        onValueChange = onRoomNameChange,
                        hint = "Room name",
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                        ),
                    )
                    CreateRoomTextField(
                        state = description,
                        onValueChange = onDescriptionChange,
                        hint = "Description",
                        minLines = 3,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                    )

                    Spacer(Modifier.height(8f.dp))
                    Box(
                        Modifier
                            .width(120f.dp)
                            .aspectRatio(1f)
                            .clip(MaterialTheme.shapes.extraLarge)
                            .clickable { showBottomSheet = true }
                    ) {
//                        Image(
//                            imageUrl = "",
//                            placeholder = Res.drawable.blank,
//                            modifier = Modifier.fillMaxSize(),
//                        )
                        AsyncImage(
                            remember(image) {
                                image?.snapshot()?.also {
                                    println("Image size is ${it.size}")
                                }?.toByteArray()
                            },
                            null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Icon(
                            Icons.Outlined.PhotoCamera, null,
                            tint = Color.White.copy(alpha = 0.6f),
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.15f))
                                .fillMaxSize()
                                .padding(16f.dp)
                        )
                    }
                }
            }

            Button(
                onClick = onSubmit,
                modifier = Modifier.align(Alignment.End),
                enabled = !state.isLoading,
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(text = "Submit")
                }
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val onComplete = { imageBuffer: Buffer ->
                    setImage(imageBuffer)
                    showBottomSheet = false
                }
                TakePictureItem(onComplete = onComplete)
                ChoosePictureItem(onComplete = onComplete)
                Spacer(Modifier.height(16f.dp))
            }
        }
    }

    LaunchedEffect(state) {
        onEventReceived(state.event)
    }
}

@Composable
expect fun ChoosePictureItem(
    modifier: Modifier = Modifier,
    onComplete: (Buffer) -> Unit
)

@Composable
expect fun TakePictureItem(
    modifier: Modifier = Modifier,
    onComplete: (Buffer) -> Unit
)

@Composable
private fun CreateRoomTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    hint: String,
    singleLine: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    Column(modifier = modifier) {
        TextField(
            value = state.value,
            onValueChange = onValueChange,
            placeholder = hint,
            singleLine = singleLine,
            minLines = minLines,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1f.dp,
                    color = if (state.isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface.copy(
                        0.3f
                    ),
                    shape = MaterialTheme.shapes.extraLarge
                )
                .padding(horizontal = 16f.dp, vertical = 12f.dp),
        )
        state.errorMessage?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 8f.dp).padding(top = 2.dp, bottom = 8.dp)
            )
        }
    }
}

@Composable
fun BottomSheetMenuItem(
    text: String,
    onTap: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier
            .clickable(onClick = onTap)
            .fillMaxWidth()
            .padding(horizontal = 16f.dp, vertical = 16f.dp),
    )
}

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    GatherSpaceTheme(false) {
        CreateRoomScreen(
            onBackTap = {},
            roomName = TextFieldState(""),
            onRoomNameChange = {},
            description = TextFieldState(""),
            onDescriptionChange = {},
            image = null,
            setImage = {},
            onSubmit = {},
            onEventReceived = {},
            onAuthError = {},
            state = CreateRoomState.Initial
        )
    }
}