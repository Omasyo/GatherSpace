package com.omasyo.gatherspace.ui.createroom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

sealed interface FormState {
    data object Loading : FormState
    data object Success : FormState
    data class Error(val message: String) : FormState
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
}