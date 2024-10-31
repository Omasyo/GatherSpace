package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.Message
import io.ktor.client.*
import io.ktor.client.plugins.sse.*
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json

class MessageSocketImpl(
    private val client: HttpClient
) : MessageSocket {

    private var _onMessageReceived: (Message) -> Unit = { }

    private var closed = false

    override suspend fun connect() {
        coroutineScope {
            client.sse(path = "/rooms/1/messages") {

                incoming.collect { events ->
                    val message = Json.decodeFromString<Message>(events.data!!)
                    _onMessageReceived(message)
                }
            }
        }
    }


    override fun disconnect() {
        closed = true
    }

    override fun onMessageReceived(block: (message: Message) -> Unit) {
        _onMessageReceived = block
    }

    override suspend fun sendMessage(message: String) {
        if (closed) {
            throw IllegalStateException("Socket closed")
        }
    }

}