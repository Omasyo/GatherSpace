package com.omasyo.gatherspace.createroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.ui.components.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.io.Buffer

class CreateRoomViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CreateRoomState>(CreateRoomState.Idle)
    val state: StateFlow<CreateRoomState> = _state

    var nameField by mutableStateOf(TextFieldState(""))
        private set

    var descriptionField by mutableStateOf(TextFieldState(""))
        private set

    var image by mutableStateOf<Buffer?>(null)
        private set

    fun changeName(value: String) {
        nameField = nameField.copy(value = value)
    }

    fun changeDescription(value: String) {
        descriptionField = descriptionField.copy(value = value)
    }

    fun updateImage(image: Buffer) {
        println("Setting image $image")
        this.image = image
    }

    fun submit() {
        if (!validate()) return

        _state.value = CreateRoomState.Submitting
        viewModelScope.launch {
            roomRepository.createRoom(nameField.value, descriptionField.value).first().onError {
                _state.value = CreateRoomState.Error(it)
            }.onSuccess {
                _state.value = CreateRoomState.Submitted(it)
            }
        }
    }

    fun clearState() {
        _state.value = CreateRoomState.Idle
        nameField = TextFieldState("")
        descriptionField = TextFieldState("")
    }


    private fun validate(): Boolean {
        if (nameField.value.isBlank()) {
            nameField = nameField.copy(errorMessage = "Please enter a username")
            return false
        } else {
            nameField = nameField.copy(errorMessage = null)
        }
        return true
    }
}