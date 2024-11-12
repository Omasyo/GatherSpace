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

sealed interface FormState {
    data object Loading : FormState
    data object Success : FormState
    data class Error(val message: String) : FormState
}

@Composable
fun CreateRoomScreen(
    modifier: Modifier = Modifier,
    onRoomCreated: (Int) -> Unit,
    viewModel: CreateRoomViewModel = dependencyProvider {
        viewModel { CreateRoomViewModel(roomRepository) }
    },
) {
    LaunchedEffect(viewModel.state.collectAsStateWithLifecycle().value) {
        val state = viewModel.state.value
        if (state is CreateState.Success) {
            println("Room is created")
            onRoomCreated(state.id)
        }
    }

    CreateRoomScreen(
        modifier = modifier,
        roomName = viewModel.name,
        onRoomNameChange = viewModel::changeName,
        description = viewModel.description,
        onDescriptionChange = viewModel::changeDescription,
        onSubmit = viewModel::submit,
    )
}

@Composable
fun CreateRoomScreen(
    modifier: Modifier = Modifier,
    roomName: String,
    onRoomNameChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        TextField(value = roomName, onValueChange = onRoomNameChange)
        TextField(value = description, onValueChange = onDescriptionChange)
        Button(onClick = onSubmit) {
            Text(text = "Submit")
        }
    }
}