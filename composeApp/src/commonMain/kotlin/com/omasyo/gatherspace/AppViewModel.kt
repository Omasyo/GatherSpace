package com.omasyo.gatherspace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omasyo.gatherspace.domain.auth.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    val isAuthenticated = authRepository.isAuthenticated()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().first()
        }
    }
}