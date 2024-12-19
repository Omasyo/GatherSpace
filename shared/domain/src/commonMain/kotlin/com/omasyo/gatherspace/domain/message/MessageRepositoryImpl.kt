package com.omasyo.gatherspace.domain.message

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.omasyo.gatherspace.domain.DomainResponse
import com.omasyo.gatherspace.domain.mapToDomain
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

internal class MessageRepositoryImpl(
    private val networkSource: MessageNetworkSource,
    private val dispatcher: CoroutineDispatcher
) : MessageRepository {
    override fun getRecentMessages(
        roomId: Int
    ): Flow<PagingData<Message>> = Pager(PagingConfig(pageSize = 5)) {
        MessagePagingSource(
            roomId,
            networkSource
        )
    }.flow.flowOn(dispatcher)

    override fun getMessageFlow(roomId: Int): Flow<Message> =
        networkSource.getMessageFlow(roomId).flowOn(dispatcher)


    override fun sendMessage(roomId: Int, message: String): Flow<DomainResponse<Unit>> =
        flow {
            emit(networkSource.sendMessage(roomId, message).mapToDomain())
        }.flowOn(dispatcher)
}