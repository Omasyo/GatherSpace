package com.omasyo.gatherspace.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.cachedIn
import com.omasyo.gatherspace.RoomR
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.onSuccess
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoomViewModel(
    private val roomId: Int,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val refreshRoomEvent = MutableStateFlow(Any())

    @OptIn(ExperimentalCoroutinesApi::class)
    val room: StateFlow<UiState<RoomDetails>> = refreshRoomEvent.flatMapLatest {
        roomRepository.getRoom(roomId)
            .map {
                when (it) {
                    is DomainError -> UiState.Error(it.message)
                    AuthError -> UiState.Error("Invalid State")
                    is Success -> UiState.Success(it.data)
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), UiState.Loading)


    val oldMessages = messageRepository.getRecentMessages(roomId).cachedIn(viewModelScope)

    val messages = mutableStateListOf<Message>()

    var message by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            messageRepository.getMessageFlow(roomId)
                .catch {
                    //TODO donerror stuffs here
                }
                .collect {
                    messages.add(it)
                }
        }
    }

    fun changeMessage(message: String) {
        this.message = message
    }

    fun refreshRoom() {
        viewModelScope.launch {
            refreshRoomEvent.emit(Any())
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            messageRepository.sendMessage(roomId, message).first()
                .onSuccess {
                    message = ""
                }
        }
    }
}