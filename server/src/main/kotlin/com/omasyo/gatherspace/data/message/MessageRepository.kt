package com.omasyo.gatherspace.data.message

import com.omasyo.gatherspace.database.Database
import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun createMessageRepository(database: Database): MessageRepository =
    MessageRepositoryImpl(database.messageQueries)

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
