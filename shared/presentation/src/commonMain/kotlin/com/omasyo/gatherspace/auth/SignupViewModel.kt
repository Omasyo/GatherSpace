package com.omasyo.gatherspace.auth

import com.omasyo.gatherspace.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface SignupViewModel {
    val coroutineScope: CoroutineScope

    val state: StateFlow<AuthState>

    val usernameField: TextFieldState

    val passwordField: TextFieldState

    fun changeUsername(value: String)

    fun changePassword(value: String)

    fun submit()

    fun clearEvent()
}