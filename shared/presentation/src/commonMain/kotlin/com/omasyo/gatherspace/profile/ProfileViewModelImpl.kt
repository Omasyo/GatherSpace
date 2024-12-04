package com.omasyo.gatherspace.profile

import com.omasyo.gatherspace.domain.AuthError
import com.omasyo.gatherspace.domain.DomainError
import com.omasyo.gatherspace.domain.Success
import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.models.response.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.io.Buffer

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelImpl(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    override val coroutineScope: CoroutineScope = MainScope()
) : ProfileViewModel {

    private val refreshEvent = MutableStateFlow(Any())

    override val userDetails: StateFlow<UserDetails?> = refreshEvent.flatMapLatest {
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
    }.stateIn(coroutineScope, SharingStarted.Lazily, null)

    override val userSessions: StateFlow<List<UserSession>> = refreshEvent.flatMapLatest {
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
    }.stateIn(coroutineScope, SharingStarted.Lazily, emptyList())

    private val _state = MutableStateFlow(ProfileScreenState.Initial)
    override val state = _state.asStateFlow()

    override fun updateImage(buffer: Buffer) {
        coroutineScope.launch {
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

    override fun logout() {
        coroutineScope.launch {
            when (authRepository.logout().first()) {
                AuthError -> _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                is DomainError -> _state.value = _state.value.copy(event = ProfileScreenEvent.Error("Logout failed"))
                is Success -> _state.value = _state.value.copy(event = ProfileScreenEvent.Logout)
            }
        }
    }

    override fun logoutSession(session: UserSession) {
        coroutineScope.launch {
            when (userRepository.deleteUserSession(session.deviceId).first()) {
                AuthError -> _state.value = _state.value.copy(event = ProfileScreenEvent.AuthError)
                is DomainError -> _state.value =
                    _state.value.copy(event = ProfileScreenEvent.Error("Couldn't delete session"))

                is Success -> _state.value =
                    _state.value.copy(event = ProfileScreenEvent.SessionLogout(session.deviceName))
            }
        }
    }

    override fun onEventReceived(event: ProfileScreenEvent) {
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