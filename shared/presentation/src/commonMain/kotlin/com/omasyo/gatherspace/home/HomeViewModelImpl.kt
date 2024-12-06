package com.omasyo.gatherspace.home

import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*

class HomeViewModelImpl(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
    authRepository: AuthRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : HomeViewModel {
    private val refreshRoomsEvent = MutableStateFlow(Any())

    override val isAuthenticated: StateFlow<Boolean> = authRepository.isAuthenticated()
        .stateIn(coroutineScope, SharingStarted.WhileSubscribed(), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val user: StateFlow<UiState<UserDetails>> =
        refreshRoomsEvent.flatMapLatest {
            userRepository.getCurrentUser()
                .map {
                    when (it) {
                        is DomainError -> UiState.Error(it.message)
                        AuthError -> UiState.Error("Invalid State")
                        is Success -> UiState.Success(it.data)
                    }
                }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), UiState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    override val allRooms: StateFlow<UiState<List<Room>>> =
        refreshRoomsEvent.flatMapLatest {
            roomRepository.getAllRooms()
                .map {
                    when (it) {
                        is DomainError -> UiState.Error(it.message)
                        AuthError -> UiState.Error("Invalid State")
                        is Success -> UiState.Success(it.data)
                    }
                }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), UiState.Loading)


    @OptIn(ExperimentalCoroutinesApi::class)
    override val userRooms: StateFlow<UiState<List<Room>>> =
        refreshRoomsEvent.flatMapLatest {
            roomRepository.getUserRooms()
                .map {
                    when (it) {
                        is DomainError -> UiState.Error(it.message)
                        AuthError -> UiState.Error("User not authenticated")
                        is Success -> UiState.Success(it.data)
                    }
                }
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), UiState.Loading)

    override fun refresh() {
        refreshRoomsEvent.tryEmit(Any())
    }
}
