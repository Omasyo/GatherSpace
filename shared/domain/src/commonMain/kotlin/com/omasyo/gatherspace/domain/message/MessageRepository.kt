package com.omasyo.gatherspace.domain.message

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MessageRepository {
    fun getRecentMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int
    ): Flow<DomainResponse<List<Message>>>

    fun getMessageFlow(roomId: Int): Flow<Message>

    fun sendMessage(roomId: Int, message: String): Flow<DomainResponse<Unit>>
}