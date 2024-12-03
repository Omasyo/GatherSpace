package com.omasyo.gatherspace.createroom

data class CreateRoomState(
    val isLoading: Boolean,
    val event: CreateRoomEvent,
) {
    companion object {
        val Initial = CreateRoomState(isLoading = false, event = CreateRoomEvent.None)
    }
}

sealed interface CreateRoomEvent {
    data object None : CreateRoomEvent
    data class Success(val id: Int) : CreateRoomEvent
    data class Error(val message: String) : CreateRoomEvent
    data object AuthError : CreateRoomEvent
}