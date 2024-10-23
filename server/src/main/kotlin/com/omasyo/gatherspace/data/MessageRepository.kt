package com.omasyo.gatherspace.data

import java.time.LocalDateTime
import com.omasyo.gatherspace.models.Message
import kotlinx.datetime.toKotlinLocalDateTime
import org.postgresql.util.PSQLException

fun createMessageRepository(database: Database): MessageRepository =
    MessageRepositoryImpl(database.messageQueries)

interface MessageRepository {
    fun create(content: String, senderId: Int?, roomId: Int): DatabaseResponse<Unit>

    fun getMessages(roomId: Int, before: LocalDateTime, limit: Int): List<Message>
}

private class MessageRepositoryImpl(private val db: MessageQueries) : MessageRepository {
    override fun create(content: String, senderId: Int?, roomId: Int): DatabaseResponse<Unit> {
        try {
            db.create(content, senderId, roomId)
            return Success(Unit)
        } catch (e: PSQLException) {
            println("Omas is printing ${e.serverErrorMessage?.detail}")
            return ErrorObject(e.serverErrorMessage?.detail ?: "")
        }
    }

    override fun getMessages(roomId: Int, before: LocalDateTime, limit: Int): List<Message> {
        return db.messagesWithRoomId(
            roomId = roomId,
            before = before,
            limit = limit.toLong(),
            mapper = { id, content, senderId, created, modified ->
                Message(
                    id,
                    content,
                    senderId,
                    roomId,
                    created.toKotlinLocalDateTime(),
                    modified.toKotlinLocalDateTime()
                )
            }
        ).executeAsList()
    }
}