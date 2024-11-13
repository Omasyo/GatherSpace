package com.omasyo.gatherspace.data.message

import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MessageRepository {
    fun create(
        content: String, senderId: Int?, roomId: Int
    ): Message

    fun getMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int,
    ): List<Message>

    fun lastMessageFlow(roomId: Int): Flow<Message>
}
