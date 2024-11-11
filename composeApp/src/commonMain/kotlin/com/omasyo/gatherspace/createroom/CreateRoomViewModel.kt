package com.omasyo.gatherspace.createroom

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreateRoomViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val _state = MutableStateFlow<CreateState>(CreateState.Idle)
    val state: StateFlow<CreateState> = _state

    var name by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    fun changeName(value: String) {
        name = value
    }

    fun changeDescription(value: String) {
        description = value
    }

    fun submit() {
        _state.value = CreateState.Loading
        viewModelScope.launch {
            roomRepository.createRoom(name, description).first().onError {
                _state.value = CreateState.Error(it)
            }.onSuccess {
                _state.value = CreateState.Success(it)
            }
        }
    }
}


sealed interface CreateState {
    data object Idle : CreateState
    data object Loading : CreateState
    data class Success(val id: Int) : CreateState
    data class Error(val message: String) : CreateState
}