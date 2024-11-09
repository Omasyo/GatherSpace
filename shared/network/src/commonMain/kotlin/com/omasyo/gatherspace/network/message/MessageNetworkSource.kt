package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.NetworkSource
import com.omasyo.gatherspace.network.room.client
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDateTime

fun createMessageNetworkSource(): MessageNetworkSource = MessageNetworkSourceImpl(client)

interface MessageNetworkSource : NetworkSource {
    suspend fun getRecentMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int
    ): Result<List<Message>>

    fun getMessageFlow(roomId: Int): Flow<Message>

    suspend fun sendMessage(roomId: Int, message: String): Result<Unit>
}
