package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.network.NetworkSource
import kotlinx.coroutines.flow.Flow

interface MessageNetworkSource : NetworkSource {
    fun getRecentMessages(): List<Message>

    fun getMessageFlow(roomId: Int): Flow<Message>

    suspend fun sendMessage(roomId: Int, message: String): Result<Unit>

}
