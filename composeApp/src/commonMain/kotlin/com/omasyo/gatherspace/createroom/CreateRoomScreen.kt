package com.omasyo.gatherspace.createroom

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.ui.components.TextFieldState
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme

@Composable
fun CreateRoomRoute(
    modifier: Modifier = Modifier,
    onRoomCreated: (Int) -> Unit,
    onAuthError: () -> Unit,
    viewModel: CreateRoomViewModel = dependencyProvider {
        viewModel { CreateRoomViewModel(roomRepository) }
    },
) {
    CreateRoomScreen(
        modifier = modifier,
        roomName = viewModel.nameField,
        onRoomNameChange = viewModel::changeName,
        description = viewModel.descriptionField,
        onDescriptionChange = viewModel::changeDescription,
        onSubmit = viewModel::submit,
        onRoomCreated = {
            viewModel.clearState()
            onRoomCreated(it)
        },
        onAuthError = onAuthError,
        state = viewModel.state.collectAsStateWithLifecycle().value
    )
}

@Composable
fun CreateRoomScreen(
    modifier: Modifier = Modifier,
    roomName: TextFieldState,
    onRoomNameChange: (String) -> Unit,
    description: TextFieldState,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onRoomCreated: (Int) -> Unit,
    onAuthError: () -> Unit,
    state: CreateRoomState
) {
    Scaffold {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16f.dp, vertical = 72f.dp),
            verticalArrangement = Arrangement.spacedBy(8f.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Create Room",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            CreateRoomTextField(
                state = roomName,
                onValueChange = onRoomNameChange,
                hint = "Room name",
                maxLines = 1,
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
            Button(onClick = onSubmit) {
                Text(text = "Submit")
            }
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is CreateRoomState.Error -> onAuthError()
            is CreateRoomState.Submitted -> onRoomCreated(state.id)
            else -> {}
        }
    }
}

@Composable
private fun CreateRoomTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    onValueChange: (String) -> Unit,
    hint: String,
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

@Preview
@Composable
private fun CreateRoomScreenPreview() {
    GatherSpaceTheme(false) {
        CreateRoomScreen(
            roomName = TextFieldState(""),
            onRoomNameChange = {},
            description = TextFieldState(""),
            onDescriptionChange = {},
            onSubmit = {},
            onRoomCreated = {},
            onAuthError = {},
            state = CreateRoomState.Idle
        )
    }
}