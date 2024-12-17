package com.omasyo.gatherspace.room

import app.cash.paging.PagingData
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RoomViewModel {

    val coroutineScope: CoroutineScope

    val state: StateFlow<RoomState>

    val room: StateFlow<RoomDetails?>

    val oldMessages: Flow<PagingData<Message>>

    val messages: List<Message>

    val message: String

    fun changeMessage(message: String)

    fun joinRoom()

    fun sendMessage()

    fun onEventReceived(event: RoomEvent)
}