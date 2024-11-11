package com.omasyo.gatherspace.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
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
            _state.value = when (val response = authRepository.login(username, password).first()) {
                is DomainError -> AuthState.Error(response.message)
                AuthError -> TODO()
                is Success -> AuthState.Success
            }
        }
    }
}

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data object Success : AuthState
    data class Error(val message: String) : AuthState
}