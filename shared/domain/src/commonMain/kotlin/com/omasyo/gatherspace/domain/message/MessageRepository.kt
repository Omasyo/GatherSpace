package com.omasyo.gatherspace.domain.message

import app.cash.paging.PagingData
import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

fun MessageRepository(
    networkSource: MessageNetworkSource,
    dispatcher: CoroutineDispatcher
): MessageRepository = MessageRepositoryImpl(networkSource, dispatcher)

interface MessageRepository {
    fun getRecentMessages(
        roomId: Int
    ): Flow<PagingData<Message>>

    fun getMessageFlow(roomId: Int): Flow<Message>

    fun sendMessage(roomId: Int, message: String): Flow<DomainResponse<Unit>>
}