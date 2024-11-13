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
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json

internal class MessageNetworkSourceImpl(
    private val client: HttpClient
) : MessageNetworkSource {
    override suspend fun getRecentMessages(
        roomId: Int,
        before: LocalDateTime,
        limit: Int
    ): Result<List<Message>> = mapResponse {
        client.get(Messages(Rooms.Id(roomId), before, limit))
    }

    override fun getMessageFlow(roomId: Int): Flow<Message> =
        flow {
            client.sse(path = "/rooms/$roomId/messages/events") {
                incoming.collect { events ->
                    println("MessageNetworkSourceImpl:getMessageFlow: $events")

                    when (events.event) {
                        "connect" -> {
                            println("MessageNetworkSourceImpl:getMessageFlow: Connected to $roomId")
                        }

                        "message" -> {
                            val message = Json.decodeFromString<Message>(events.data!!)
                            emit(message)
                        }
                    }
                }
            }
        }

    override suspend fun sendMessage(roomId: Int, message: String): Result<Unit> =
        mapResponse {

            client.post(Messages(Rooms.Id(roomId))) {
                setBody(MessageRequest(message))
            }
//            client.post("/rooms/${roomId}/messages") {
//                setBody(MessageRequest(message))
//            }
        }


}