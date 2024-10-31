package com.omasyo.gatherspace.data.message

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.omasyo.gatherspace.database.MessageQueries
import com.omasyo.gatherspace.models.response.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

internal class MessageRepositoryImpl(private val messageQueries: MessageQueries) : MessageRepository {
    override fun create(
        content: String, senderId: Int?, roomId: Int
    ): Message {
        return messageQueries.create(
            content = content,
            sender_id = senderId,
            room_id = roomId,
            mapper = ::messageMapper
        ).executeAsOne()
    }

    override fun getMessages(
        roomId: Int, before: kotlinx.datetime.LocalDateTime, limit: Int
    ): List<Message> {
        return messageQueries.messagesWithRoomId(
            roomId = roomId,
            before = before.toJavaLocalDateTime(),
            limit = limit.toLong(),
            mapper = ::messageMapper
        ).executeAsList()
    }

    override fun lastMessageFlow(roomId: Int): Flow<Message> {
        return messageQueries.lastMessage(roomId, mapper = ::messageMapper)
            .asFlow().mapToOne(Dispatchers.IO)
    }

    private fun messageMapper(
        id: Int,
        content: String,
        senderId: Int?,
        roomId: Int,
        created: LocalDateTime,
        modified: LocalDateTime
    ): Message = Message(
        id, content, senderId, roomId, created.toKotlinLocalDateTime(), modified.toKotlinLocalDateTime()
    )
}