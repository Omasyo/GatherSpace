package com.omasyo.gatherspace.home

import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.UserDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface HomeViewModel {
    val coroutineScope: CoroutineScope

    val isAuthenticated: StateFlow<Boolean>

    val user: StateFlow<UiState<UserDetails>>

    val allRooms: StateFlow<UiState<List<Room>>>

    val userRooms: StateFlow<UiState<List<Room>>>

    fun refresh()
}