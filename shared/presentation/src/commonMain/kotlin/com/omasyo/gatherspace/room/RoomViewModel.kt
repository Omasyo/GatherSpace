package com.omasyo.gatherspace.room

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import com.omasyo.gatherspace.domain.*
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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