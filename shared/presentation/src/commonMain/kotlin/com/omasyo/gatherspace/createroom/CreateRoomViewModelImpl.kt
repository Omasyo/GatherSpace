package com.omasyo.gatherspace.createroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.domain.onError
import com.omasyo.gatherspace.domain.onSuccess
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.io.Buffer

class CreateRoomViewModelImpl(
    private val roomRepository: RoomRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : CreateRoomViewModel {

    private val _state = MutableStateFlow(CreateRoomState.Initial)
    override val state: StateFlow<CreateRoomState> = _state

    override var nameField by mutableStateOf(TextFieldState(""))
        private set

    override var descriptionField by mutableStateOf(TextFieldState(""))
        private set

    override var image by mutableStateOf<Buffer?>(null)
        private set

    override fun changeName(value: String) {
        nameField = nameField.copy(value = value)
    }

    override fun changeDescription(value: String) {
        descriptionField = descriptionField.copy(value = value)
    }

    override fun updateImage(image: Buffer) {
        this.image = image
    }

    override fun submit() {
        if (!validate()) return

        _state.value = _state.value.copy(isLoading = true)
        coroutineScope.launch {
            roomRepository.createRoom(nameField.value, descriptionField.value, image).first().onError {
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(event = CreateRoomEvent.Error(it))
            }.onSuccess {
                _state.value = _state.value.copy(event = CreateRoomEvent.Success(it))
            }
        }
    }

    override fun onEventReceived(event: CreateRoomEvent) {
        when (event) {
            is CreateRoomEvent.Error, CreateRoomEvent.AuthError -> {
                _state.value = _state.value.copy(event = CreateRoomEvent.None)
            }

            is CreateRoomEvent.Success -> {
                _state.value = CreateRoomState.Initial
                nameField = TextFieldState("")
                descriptionField = TextFieldState("")
                image = null
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