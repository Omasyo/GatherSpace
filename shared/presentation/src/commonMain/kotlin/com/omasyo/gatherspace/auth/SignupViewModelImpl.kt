package com.omasyo.gatherspace.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.omasyo.gatherspace.TextFieldState
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SignupViewModelImpl(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : SignupViewModel {
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
            userRepository.createAccount(usernameField.value, passwordField.value, null).first()
                .onError {
                    _state.value = _state.value.copy(event = AuthEvent.Error(it))
                }
                .onSuccess {
                    _state.value =
                        when (val response = authRepository.login(usernameField.value, passwordField.value).first()) {
                            is DomainError -> _state.value.copy(event = AuthEvent.Error(response.message))
                            is Success -> _state.value.copy(event = AuthEvent.Success)
                            AuthError -> throw IllegalStateException()
                        }
                }
            _state.value = _state.value.copy(isLoading = true)

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
        } else if (passwordField.value.contains(Regex("\\s"))) {
            passwordField = passwordField.copy(errorMessage = "Password cannot contain whitespace")
            isValid = false
        } else if (passwordField.value.length < 3) {
            passwordField = passwordField.copy(errorMessage = "Password should be at least 3 characters")
            isValid = false
        } else {
            passwordField = passwordField.copy(errorMessage = null)
        }
        return isValid
    }
}