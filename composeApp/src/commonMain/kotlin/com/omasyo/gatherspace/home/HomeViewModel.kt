package com.omasyo.gatherspace.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val refreshRoomsEvent = MutableStateFlow(Any())

    @OptIn(ExperimentalCoroutinesApi::class)
    val rooms: StateFlow<UiState<List<Room>>> =
        refreshRoomsEvent.flatMapLatest {
            roomRepository.getRooms()
                .map {
                    when (it) {
                        is DomainError -> UiState.Error(it.message)
                        AuthError -> UiState.Error("Invalid State")
                        is Success -> UiState.Success(it.data)
                    }
                }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Loading)

//    val user: StateFlow<UiState<User>> =
//        userRepository

    fun refreshRooms() {
        viewModelScope.launch {
            refreshRoomsEvent.emit(Any())
        }
    }
}

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Error(val reason: String) : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
}
