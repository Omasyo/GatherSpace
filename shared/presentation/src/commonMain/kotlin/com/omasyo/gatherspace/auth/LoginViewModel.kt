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

interface LoginViewModel {
    val coroutineScope: CoroutineScope

    val state: StateFlow<AuthState>

    val usernameField: TextFieldState

    val passwordField: TextFieldState

    fun changeUsername(value: String)

    fun changePassword(value: String)

    fun submit()

    fun clearEvent()
}