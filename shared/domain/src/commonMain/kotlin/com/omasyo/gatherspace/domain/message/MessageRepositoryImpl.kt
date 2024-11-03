package com.omasyo.gatherspace.domain.message

import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.LocalDateTime

class MessageRepositoryImpl(
    private val networkSource: MessageNetworkSource
) : MessageRepository {
    override fun getRecentMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int
    ): Flow<DomainResponse<List<Message>>> = flow {
        emit(networkSource.getRecentMessages(roomId, before, limit).mapToDomain())
    }

    override fun getMessageFlow(roomId: Int): Flow<Message> {
        return networkSource.getMessageFlow(roomId)
    }

    override fun sendMessage(roomId: Int, message: String): Flow<DomainResponse<Unit>> = flow {
        emit(networkSource.sendMessage(roomId, message).mapToDomain())
    }
}