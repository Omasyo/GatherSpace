package com.omasyo.gatherspace.createroom

sealed interface CreateRoomState {
    data object Idle : CreateRoomState
    data object Submitting : CreateRoomState
    data class Submitted(val id: Int) : CreateRoomState
    data class Error(val message: String) : CreateRoomState
}