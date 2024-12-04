package com.omasyo.gatherspace.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ComposeProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel(), ProfileViewModel by ProfileViewModelImpl(authRepository, userRepository) {
    override val coroutineScope: CoroutineScope
        get() = viewModelScope
}