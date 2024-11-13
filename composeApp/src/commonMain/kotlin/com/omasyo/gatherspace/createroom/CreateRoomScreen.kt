package com.omasyo.gatherspace.createroom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.ui.components.SubmitState
import com.omasyo.gatherspace.ui.components.TextFieldState

sealed interface FormState {
    data object Loading : FormState
    data object Success : FormState
    data class Error(val message: String) : FormState
}

@Composable
fun CreateRoomScreen(
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
        onRoomCreated = onRoomCreated,
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
    state: SubmitState
) {
    Column(modifier = modifier.fillMaxSize()) {
        TextField(
            value = roomName.value,
            onValueChange = onRoomNameChange,
            supportingText = roomName.errorMessage?.let { { Text(it) } },
            isError = roomName.isError
        )
        TextField(
            value = description.value,
            onValueChange = onDescriptionChange,
            supportingText = description.errorMessage?.let { { Text(it) } },
            isError = description.isError
        )
        Button(onClick = onSubmit) {
            Text(text = "Submit")
        }
    }

    LaunchedEffect(state) {
        when (state) {
            is SubmitState.Error -> onAuthError()
            is SubmitState.Submitted -> onRoomCreated(state.id)
            else -> {}
        }
    }
}