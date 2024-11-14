package com.omasyo.gatherspace.home

sealed interface SendMessageState {
    data object Idle : SendMessageState
    data object Loading : SendMessageState
    data class Error(val message: String) : SendMessageState
}