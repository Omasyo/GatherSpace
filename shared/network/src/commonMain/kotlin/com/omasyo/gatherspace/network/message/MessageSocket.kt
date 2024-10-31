package com.omasyo.gatherspace.network.message

import com.omasyo.gatherspace.models.Message

interface MessageSocket {

    suspend fun connect()

    fun disconnect()

    fun onMessageReceived(block: (message: Message) -> Unit)

    suspend fun sendMessage(message: String)

}
