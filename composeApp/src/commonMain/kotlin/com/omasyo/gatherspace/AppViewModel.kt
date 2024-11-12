package com.omasyo.gatherspace

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.domain.auth.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.properties.Delegates

class AppViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private var initialAuthState by Delegates.notNull<Boolean>()
        private set

    init {
        runBlocking {
//            initialSchemeId = settingsRepository.getColorSchemeId().first()
            initialAuthState = !authRepository.isAuthenticated().first()
        }
    }

    val isAuthenticated = authRepository.isAuthenticated()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), initialAuthState)
}