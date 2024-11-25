package com.omasyo.gatherspace.auth

data class AuthState(
    val isLoading: Boolean,
    val event: AuthEvent,
) {
    companion object {
        val Initial = AuthState(isLoading = false, event = AuthEvent.None)
    }
}

sealed interface AuthEvent {
    data object None : AuthEvent
    data object Success : AuthEvent
    data class Error(val message: String) : AuthEvent
}