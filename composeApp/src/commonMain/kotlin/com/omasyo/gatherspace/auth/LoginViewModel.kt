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
import com.omasyo.gatherspace.ui.components.TextFieldState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    var usernameField by mutableStateOf(TextFieldState(""))
        private set

    var passwordField by mutableStateOf(TextFieldState(""))
        private set

    fun changeUsername(value: String) {
        usernameField = usernameField.copy(value = value)
    }

    fun changePassword(value: String) {
        passwordField = passwordField.copy(value = value)
    }

    fun submit() {
        if (!validate()) return

        _state.value = AuthState.Loading
        viewModelScope.launch {
            _state.value =
                when (val response = authRepository.login(usernameField.value, passwordField.value).first()) {
                    is DomainError -> AuthState.Error(response.message)
                    AuthError -> throw IllegalStateException()
                    is Success -> AuthState.Success
                }
        }
    }

    private fun validate(): Boolean {
        var isValid = true
        if (usernameField.value.isEmpty()) {
            usernameField = usernameField.copy(errorMessage = "Username cannot be empty")
            isValid = false
        } else {
            usernameField = usernameField.copy(errorMessage = null)
        }

        if (passwordField.value.isEmpty()) {
            passwordField = passwordField.copy(errorMessage = "Password cannot be empty")
            isValid = false
        } else {
            passwordField = passwordField.copy(errorMessage = null)
        }
        
        return isValid
    }
}