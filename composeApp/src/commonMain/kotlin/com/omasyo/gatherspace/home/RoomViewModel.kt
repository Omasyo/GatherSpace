package com.omasyo.gatherspace.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.RoomR
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoomViewModel(
    private val roomRoute: RoomR,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val refreshRoomEvent = MutableStateFlow(Unit)

    @OptIn(ExperimentalCoroutinesApi::class)
    val room: StateFlow<UiState<RoomDetails>> = refreshRoomEvent.flatMapLatest {
        roomRepository.getRoom(roomRoute.id)
            .map {
                when (it) {
                    is DomainError -> UiState.Error(it.message)
                    AuthError -> UiState.Error("Invalid State")
                    is Success -> UiState.Success(it.data)
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Loading)


    val oldMessages = messageRepository.getRecentMessages(roomRoute.id)

    val messages = mutableStateListOf<Message>()

    init {
        viewModelScope.launch {
            messageRepository.getMessageFlow(roomRoute.id)
                .collect {
                    messages.add(it)
                }
        }
    }

    fun refreshRooms() {
        viewModelScope.launch {
            refreshRoomEvent.emit(Unit)
        }
    }
}