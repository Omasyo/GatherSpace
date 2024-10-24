package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.MessageRequest
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MessageSocketImpl(
    private val client: HttpClient
) : MessageSocket {

    private var _onMessageReceived: (MessageRequest) -> Unit = { }

    private val messageFlow = MutableSharedFlow<MessageRequest>()
    private var closed = false

    override suspend fun connect() {
        coroutineScope {
            client.webSocket(method = HttpMethod.Get, path = "/rooms/1") {
                launch {
                    messageFlow.collect {
                        sendSerialized(it)
                    }
                }
                while (true) {
                    if (closed) {
                        break
                    }
                    val othersMessage = receiveDeserialized<MessageRequest>()
                    _onMessageReceived(othersMessage)
                }
            }
        }
    }


    override fun disconnect() {
        closed = true
    }

    override fun onMessageReceived(block: (message: MessageRequest) -> Unit) {
        _onMessageReceived = block
    }

    override suspend fun sendMessage(message: String, senderId: Int?) {
        if (closed) {
            throw IllegalStateException("Socket closed")
        }
        messageFlow.emit(MessageRequest(senderId, message))
    }

}