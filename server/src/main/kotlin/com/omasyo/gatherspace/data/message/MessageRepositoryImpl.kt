package com.omasyo.gatherspace.data.message

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import com.omasyo.gatherspace.database.MessageQueries
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.utils.getImagePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

internal class MessageRepositoryImpl(private val messageQueries: MessageQueries) : MessageRepository {
    override fun create(
        content: String, senderId: Int?, roomId: Int
    ): Message {
        val id = messageQueries.create(
            content = content,
            sender_id = senderId,
            room_id = roomId,
        ).executeAsOne()
        return messageQueries.getById(id, ::messageMapper).executeAsOne()
    }

    override fun getMessages(
        roomId: Int, before: kotlinx.datetime.LocalDateTime, limit: Int
    ): List<Message> =
        messageQueries.messagesWithRoomId(
            roomId = roomId,
            before = before.toJavaLocalDateTime(),
            limit = limit.toLong(),
            mapper = ::messageMapper
        ).executeAsList()

    override fun lastMessageFlow(roomId: Int): Flow<Message> =
        messageQueries.lastMessage(roomId, mapper = ::messageMapper)
            .asFlow().mapToOne(Dispatchers.IO)

    private fun messageMapper(
        id: Int,
        content: String,
        senderId: Int?,
        roomId: Int,
        created: LocalDateTime,
        modified: LocalDateTime,
        username: String,
        image: String?
    ): Message = Message(
        id = id,
        content = content,
        sender = senderId?.let { User(id = senderId, username = username, imageUrl = getImagePath(image)) },
        roomId = roomId,
        created = created.toKotlinLocalDateTime(),
        modified = modified.toKotlinLocalDateTime()
    )
}