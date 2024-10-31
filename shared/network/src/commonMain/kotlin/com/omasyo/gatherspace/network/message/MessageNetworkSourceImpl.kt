package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.request.MessageRequest
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.routes.Messages
import com.omasyo.gatherspace.models.routes.Rooms
import com.omasyo.gatherspace.network.mapResponse
import io.ktor.client.*
import io.ktor.client.plugins.resources.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

internal class MessageNetworkSourceImpl(
    private val client: HttpClient
) : MessageNetworkSource {
    override fun getRecentMessages(): List<Message> {
        TODO("Not yet implemented")
    }

    override fun getMessageFlow(roomId: Int): Flow<Message> =
        flow {
            client.sse(path = "/rooms/$roomId/messages") {
                incoming.collect { events ->
                    val message = Json.decodeFromString<Message>(events.data!!)
                    emit(message)
                }
            }
        }

    override suspend fun sendMessage(roomId: Int, message: String): Result<Unit> =
        mapResponse<Unit> {

            client.post(Messages(Rooms.Id(roomId))) {
                setBody(MessageRequest(message))
            }
//            client.post("/rooms/${roomId}/messages") {
//                setBody(MessageRequest(message))
//            }
        }


}