package com.omasyo.gatherspace.ui.room

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.omasyo.gatherspace.Room
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.launch

class RoomViewModel(
    savedStateHandle: SavedStateHandle,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository
) : ViewModel() {

    private val roomRoute = savedStateHandle.toRoute<Room>()

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
}