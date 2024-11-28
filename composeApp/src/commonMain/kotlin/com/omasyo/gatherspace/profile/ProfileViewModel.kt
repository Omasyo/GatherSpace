package com.omasyo.gatherspace.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.io.Buffer

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val refreshEvent = MutableStateFlow(Any())

    val userDetails: StateFlow<UserDetails?> = refreshEvent.flatMapLatest {
        userRepository.getCurrentUser()
            .map {
                when (it) {
                    AuthError -> {
                        _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                        null
                    }

                    is DomainError -> {
                        _state.value = _state.value.copy(event = ProfileScreenEvent.Error(it.message))
                        null
                    }

                    is Success -> it.data
                }
            }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    val userSessions: StateFlow<List<UserSession>> = refreshEvent.flatMapLatest {
        userRepository.getUserSessions()
            .map {
                when (it) {
                    AuthError -> {
                        _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                        emptyList()
                    }

                    is DomainError -> {
                        _state.value = _state.value.copy(event = ProfileScreenEvent.Error(it.message))
                        emptyList()
                    }

                    is Success -> it.data
                }
            }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val _state = MutableStateFlow(ProfileScreenState.Initial)
    val state = _state.asStateFlow()

    fun updateImage(buffer: Buffer) {
        viewModelScope.launch {
            val response = userRepository.updateUser(
                username = null,
                password = null,
                image = buffer
            ).first()
            when (response) {
                AuthError -> _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                is DomainError -> _state.value =
                    _state.value.copy(event = ProfileScreenEvent.Error("Couldn't update image"))

                is Success -> _state.value = _state.value.copy(event = ProfileScreenEvent.ImageUpdated)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (val response = authRepository.logout().first()) {
                AuthError -> _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                is DomainError -> _state.value = _state.value.copy(event = ProfileScreenEvent.Error("Logout failed"))
                is Success -> _state.value = _state.value.copy(event = ProfileScreenEvent.Logout)
            }
        }
    }

    fun logoutSession(session: UserSession) {
        viewModelScope.launch {
            when (val response = userRepository.deleteUserSession(session.deviceId).first()) {
                AuthError -> _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                is DomainError -> _state.value =
                    _state.value.copy(event = ProfileScreenEvent.Error("Couldn't delete session"))

                is Success -> _state.value =
                    _state.value.copy(event = ProfileScreenEvent.SessionLogout(session.deviceName))
            }
        }
    }

    fun onEventReceived(event: ProfileScreenEvent) {
        when (event) {
            ProfileScreenEvent.ImageUpdated,
            is ProfileScreenEvent.SessionLogout,
            ProfileScreenEvent.Logout -> {
                refreshEvent.tryEmit(Any())
            }

            else -> Unit
        }
    }
}