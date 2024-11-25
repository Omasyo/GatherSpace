package com.omasyo.gatherspace.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.cash.paging.cachedIn
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.onError
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

    private val _state = MutableStateFlow(RoomState.Initial)
    val state: StateFlow<RoomState> = _state

    @OptIn(ExperimentalCoroutinesApi::class)
    val room: StateFlow<RoomDetails?> = refreshRoomEvent.flatMapLatest {
        roomRepository.getRoom(roomId)
            .map {
                when (it) {
                    is DomainError -> {
                        _state.value =
                            _state.value.copy(event = RoomEvent.Error(it.message))
                        null
                    }

                    AuthError -> throw IllegalStateException("Auth error")
                    is Success -> it.data
                }
            }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    val oldMessages = messageRepository.getRecentMessages(roomId).cachedIn(viewModelScope)

    val messages = mutableStateListOf<Message>()

    var message by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            messageRepository.getMessageFlow(roomId)
                .catch {
                    //TODO do error stuffs here
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

    fun joinRoom() {
        viewModelScope.launch {
            roomRepository.joinRoom(roomId).first()
                .onSuccess {
                    refreshRoom()
                    _state.value = _state.value.copy(event = RoomEvent.JoinedRoom)
                }
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isSending = true)
            messageRepository.sendMessage(roomId, message).first()
                .onError {
                    //TODO find better way of doing errors different states too much
//                    _sendMessageState.value = RoomState.Error(it)
                    _state.value = _state.value.copy(event = RoomEvent.Error(it))
                }
                .onSuccess {
                    message = ""
                    _state.value = _state.value.copy(event = RoomEvent.MessageSent)
                }

            _state.value = _state.value.copy(isSending = true)
        }
    }
}