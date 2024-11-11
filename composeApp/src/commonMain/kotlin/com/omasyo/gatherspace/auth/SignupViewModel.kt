package com.omasyo.gatherspace.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignupViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    var username by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun changeUsername(value: String) {
        username = value
    }

    fun changePassword(value: String) {
        password = value
    }

    fun submit() {
        _state.value = AuthState.Loading
        viewModelScope.launch {
            userRepository.createAccount(username, password).first()
                .onError {
                    _state.value = AuthState.Error(it)
                }
                .onSuccess {
                    _state.value = when (val response = authRepository.login(username, password).first()) {
                        is DomainError -> AuthState.Error(response.message)
                        AuthError -> TODO()
                        is Success -> AuthState.Success
                    }
                }

        }
    }
}