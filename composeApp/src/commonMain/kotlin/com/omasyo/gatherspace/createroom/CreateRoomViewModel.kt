package com.omasyo.gatherspace.createroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.io.Buffer

class CreateRoomViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CreateRoomState.Initial)
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

        _state.value = _state.value.copy(isLoading = true)
        viewModelScope.launch {
            roomRepository.createRoom(nameField.value, descriptionField.value, image).first().onError {
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(event = CreateRoomEvent.Error(it))
            }.onSuccess {
                _state.value = _state.value.copy(event = CreateRoomEvent.Success(it))
            }
        }
    }

    fun onEventReceived(event: CreateRoomEvent) {
        when (event) {
            is CreateRoomEvent.Error -> {
                _state.value = _state.value.copy(event = CreateRoomEvent.None)
            }

            is CreateRoomEvent.Success -> {
                _state.value = CreateRoomState.Initial
                nameField = TextFieldState("")
                descriptionField = TextFieldState("")
            }

            CreateRoomEvent.None -> Unit
        }
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