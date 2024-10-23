package com.omasyo.gatherspace.data

import java.time.LocalDateTime

interface MessageRepository {
    fun create(content: String, senderId: Int, roomId: Int)

    fun getMessages(roomId: Int, before: LocalDateTime, limit: Int): List<Message>
}

private class MessageRepositoryImpl(private val db: MessageQueries) : MessageRepository {
    override fun create(content: String, senderId: Int, roomId: Int) {
        db.create(content, senderId, roomId)
    }

    override fun getMessages(roomId: Int, before: LocalDateTime, limit: Int): List<Message> {
      return  db.messagesWithRoomId(
            roomId = roomId,
            before = before,
            limit = limit.toLong()
        ).executeAsList()
    }
}

//fun MessageRepository.create() : MessageRepository =