package com.omasyo.gatherspace.createroom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.omasyo.gatherspace.CreateRoom

sealed interface FormState {
    data object Loading : FormState
    data object Success : FormState
    data class Error(val message: String) : FormState
}

@Composable
fun CreateRoomScreen(
    modifier: Modifier,
    viewModel: CreateRoomViewModel
) {

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