package com.omasyo.gatherspace.createroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.room.RoomRepository
import kotlinx.coroutines.CoroutineScope

class ComposeCreateRoomViewModel(
    private val roomRepository: RoomRepository
) : ViewModel(), CreateRoomViewModel by CreateRoomViewModelImpl(roomRepository) {
    override val coroutineScope: CoroutineScope
        get() = viewModelScope
}