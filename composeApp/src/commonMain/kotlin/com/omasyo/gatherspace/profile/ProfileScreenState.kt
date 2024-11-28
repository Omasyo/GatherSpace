package com.omasyo.gatherspace.profile

data class ProfileScreenState(
    val event: ProfileScreenEvent
) {
    companion object {
        val Initial = ProfileScreenState(ProfileScreenEvent.None)
    }
}

sealed interface ProfileScreenEvent {
    data object None : ProfileScreenEvent
    data object AuthError : ProfileScreenEvent
    data class Error(val message: String) : ProfileScreenEvent
    data object Logout : ProfileScreenEvent
    data class SessionLogout(val deviceName: String) : ProfileScreenEvent
    data object ImageUpdated : ProfileScreenEvent
}