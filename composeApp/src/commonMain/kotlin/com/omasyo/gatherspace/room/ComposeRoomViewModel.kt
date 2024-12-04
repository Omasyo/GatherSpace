package com.omasyo.gatherspace.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.CoroutineScope

class ComposeRoomViewModel(
    private val roomId: Int,
    private val messageRepository: MessageRepository,
    private val roomRepository: RoomRepository
) : ViewModel(), RoomViewModel by RoomViewModelImpl(roomId, messageRepository, roomRepository) {
    override val coroutineScope: CoroutineScope
        get() = viewModelScope
}