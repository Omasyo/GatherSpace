package com.omasyo.gatherspace.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.auth.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModelImpl(
    private val authRepository: AuthRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : LoginViewModel {

    private val _state = MutableStateFlow(AuthState.Initial)
    override val state: StateFlow<AuthState> = _state

    override var usernameField by mutableStateOf(TextFieldState(""))
        private set

    override var passwordField by mutableStateOf(TextFieldState(""))
        private set

    override fun changeUsername(value: String) {
        usernameField = usernameField.copy(value = value)
    }

    override fun changePassword(value: String) {
        passwordField = passwordField.copy(value = value)
    }

    override fun submit() {
        if (!validate()) return

        _state.value = _state.value.copy(isLoading = true)
        coroutineScope.launch {
            _state.value =
                when (val response = authRepository.login(usernameField.value, passwordField.value).first()) {
                    is DomainError -> _state.value.copy(event = AuthEvent.Error(response.message))
                    AuthError -> throw IllegalStateException()
                    is Success -> _state.value.copy(event = AuthEvent.Success)
                }
            _state.value = _state.value.copy(isLoading = false)
        }
    }

    override fun clearEvent() {
        _state.value = _state.value.copy(event = AuthEvent.None)
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