package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.MessageRequest
import com.omasyo.gatherspace.network.createClient
import com.omasyo.gatherspace.network.provideEngine
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

interface MessageSocket {

    suspend fun connect()

    fun disconnect()

    fun onMessageReceived(block: (message: MessageRequest) -> Unit)

    suspend fun sendMessage(message: String, senderId: Int?)

}

val socket: MessageSocket = MessageSocketImpl(createClient(provideEngine()))

@JsExport
class Tospi {

    fun jsConnect() {
        GlobalScope.launch {
            socket.connect()
        }
    }

    fun jsDisconnect() {
        socket.disconnect()
    }

    fun onMessageReceived(block: (message: MessageRequest) -> Unit) {
        socket.onMessageReceived(block)
    }

    fun jsSendMessage(message: String, senderId: Int? = null) {
        GlobalScope.launch {

            println("try send in scope")
            socket.sendMessage(message, senderId)
        }
    }
}


