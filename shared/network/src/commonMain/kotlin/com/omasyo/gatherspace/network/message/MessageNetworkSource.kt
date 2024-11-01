package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.NetworkSource
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

interface MessageNetworkSource : NetworkSource {
    suspend fun getRecentMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int
    ): Result<List<Message>>

    fun getMessageFlow(roomId: Int): Flow<Message>

    suspend fun sendMessage(roomId: Int, message: String): Result<Unit>

}
