package com.omasyo.gatherspace.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.cash.paging.cachedIn
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoomViewModelImpl(
    private val roomId: Int,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : RoomViewModel {

    private val refreshRoomEvent = MutableStateFlow(Any())

    private val _state = MutableStateFlow(RoomState.Initial)
    override val state: StateFlow<RoomState> = _state

    @OptIn(ExperimentalCoroutinesApi::class)
    override val room: StateFlow<RoomDetails?> = refreshRoomEvent.flatMapLatest {
        roomRepository.getRoom(roomId)
            .map {
                when (it) {
                    is DomainError -> {
                        _state.value =
                            _state.value.copy(event = RoomEvent.Error(it.message))
                        null
                    }

                    AuthError -> throw IllegalStateException("Authentication not required")
                    is Success -> it.data
                }
            }
    }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(), null)

    override val oldMessages = messageRepository.getRecentMessages(roomId).cachedIn(coroutineScope)

    override val messages = mutableStateListOf<Message>()

    override var message by mutableStateOf("")
        private set

    init {
        println("RoomView init")
        coroutineScope.launch {
            println("RoomView corout launch")
            messageRepository.getMessageFlow(roomId)
                .catch {
                    _state.value = _state.value.copy(event = RoomEvent.Error("An Error occurred"))
                }
                .collect {
                    println("Got message $it")
                    messages.add(it)
                    _state.value = _state.value.copy(event = RoomEvent.MessageReceived)
                }
        }
    }

    override fun changeMessage(message: String) {
        this.message = message
    }

    private fun refreshRoom() {
        refreshRoomEvent.tryEmit(Any())

    }

    override fun joinRoom() {
        coroutineScope.launch {
            roomRepository.joinRoom(roomId).first()
                .onSuccess {
                    refreshRoom()
                    _state.value = _state.value.copy(event = RoomEvent.JoinedRoom)
                }
        }
    }

    override fun sendMessage() {
        coroutineScope.launch {
            _state.value = _state.value.copy(isSending = true)
            messageRepository.sendMessage(roomId, message).first()
                .onError {
                    _state.value = _state.value.copy(event = RoomEvent.Error(it))
                }
                .onSuccess {
                    message = ""
//                    _state.value = _state.value.copy(event = RoomEvent.MessageSent)
                }

            _state.value = _state.value.copy(isSending = false)
        }
    }

    override fun onEventReceived(event: RoomEvent) {
        _state.value = _state.value.copy(event = RoomEvent.None)
    }
}