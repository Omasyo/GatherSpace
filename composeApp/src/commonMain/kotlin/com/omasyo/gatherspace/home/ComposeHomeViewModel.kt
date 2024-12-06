package com.omasyo.gatherspace.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.CoroutineScope

class ComposeHomeViewModel(
    roomRepository: RoomRepository,
    userRepository: UserRepository,
    authRepository: AuthRepository,
) : ViewModel(), HomeViewModel by HomeViewModelImpl(roomRepository, userRepository, authRepository) {
    override val coroutineScope: CoroutineScope = viewModelScope
}