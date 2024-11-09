package com.omasyo.gatherspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.model.Room
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.flow.*

class HomeViewModel(
    private val roomRepository: RoomRepository
) : ViewModel() {
    val rooms: StateFlow<RoomState> =
        roomRepository.getRooms()
            .map {
                when (it) {
                    is DomainError -> RoomState.Error(it.message)
                    AuthError -> RoomState.Error("Invalid State")
                    is Success -> RoomState.Success(it.data)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RoomState.Loading)

}

sealed interface RefreshState {
    data object Loading : RefreshState
    data class Error(val message: String) : RefreshState
    data object Success: RefreshState
}