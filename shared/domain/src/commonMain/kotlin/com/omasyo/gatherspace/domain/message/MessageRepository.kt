package com.omasyo.gatherspace.domain.message

import app.cash.paging.PagingData
import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getRecentMessages(
        roomId: Int
    ): Flow<PagingData<Message>>

    fun getMessageFlow(roomId: Int): Flow<Message>

    fun sendMessage(roomId: Int, message: String): Flow<DomainResponse<Unit>>
}