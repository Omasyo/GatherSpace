package com.omasyo.gatherspace.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.CoroutineScope

class ComposeHomeViewModel(
    private val roomRepository: RoomRepository,
    private val userRepository: UserRepository,
) : ViewModel(), HomeViewModel by HomeViewModelImpl(roomRepository, userRepository) {
    override val coroutineScope: CoroutineScope = viewModelScope
}