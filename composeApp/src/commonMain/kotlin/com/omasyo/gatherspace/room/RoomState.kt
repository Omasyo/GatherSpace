package com.omasyo.gatherspace.room

data class RoomState(
    val isSending: Boolean,
    val event: RoomEvent
) {
    companion object {
        val Initial = RoomState(
            isSending = false,
            event = RoomEvent.None
        )
    }
}

sealed interface RoomEvent {
    data object None : RoomEvent
    data object JoinedRoom : RoomEvent
    data object MessageReceived : RoomEvent
    data class Error(val message: String) : RoomEvent
}