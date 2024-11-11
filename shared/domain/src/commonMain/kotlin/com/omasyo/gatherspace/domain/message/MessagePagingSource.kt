package com.omasyo.gatherspace.domain.message

import app.cash.paging.*
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

internal class MessagePagingSource(
    private val roomId: Int,
    private val networkSource: MessageNetworkSource
) : PagingSource<LocalDateTime, Message>() {
    override fun getRefreshKey(state: PagingState<LocalDateTime, Message>): LocalDateTime? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
                ?: state.closestPageToPosition(anchorPosition)?.nextKey
        }
    }

    override suspend fun load(params: PagingSourceLoadParams<LocalDateTime>): PagingSourceLoadResult<LocalDateTime, Message> {
        return try {
            val date = params.key ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
            val response = networkSource.getRecentMessages(roomId, date, 50)
            response.getOrNull()?.let { messages ->
                PagingSourceLoadResultPage(
                    data = messages,
                    prevKey = null,
                    nextKey = messages.lastOrNull()?.created
                )
            } ?: PagingSourceLoadResultError(response.exceptionOrNull()!!)
        } catch (e: Exception) {
            println("MessagePagingSource:load: $e")
            PagingSourceLoadResultError(e)
        }
    }
}