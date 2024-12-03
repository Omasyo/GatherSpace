package com.omasyo.gatherspace.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope

class AndroidLoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel(), LoginViewModel by LoginViewModelImpl(authRepository) {
    override val coroutineScope: CoroutineScope
        get() = viewModelScope
}