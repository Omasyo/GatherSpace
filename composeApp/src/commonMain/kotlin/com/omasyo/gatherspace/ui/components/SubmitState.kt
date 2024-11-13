package com.omasyo.gatherspace.ui.components

sealed interface SubmitState {
    data object Idle : SubmitState
    data object Submitting : SubmitState
    data class Submitted(val id: Int) : SubmitState
    data class Error(val message: String) : SubmitState
}