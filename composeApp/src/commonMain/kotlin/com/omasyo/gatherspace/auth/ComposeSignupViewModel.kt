package com.omasyo.gatherspace.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.CoroutineScope

class ComposeSignupViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel(), SignupViewModel by SignupViewModelImpl(authRepository, userRepository) {
    override val coroutineScope: CoroutineScope = viewModelScope
}