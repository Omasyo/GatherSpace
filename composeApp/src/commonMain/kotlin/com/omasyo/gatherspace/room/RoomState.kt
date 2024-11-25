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
    data object MessageSent : RoomEvent
    data class Error(val message: String) : RoomEvent
}

//sealed interface RoomState {
//    data object Idle : RoomState
//    data object Loading : RoomState
//    data class Error(val message: String) : RoomState
//}