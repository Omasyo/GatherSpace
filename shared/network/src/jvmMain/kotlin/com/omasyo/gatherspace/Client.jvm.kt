package com.omasyo.gatherspace

import com.omasyo.gatherspace.models.MessageRequest
import com.omasyo.gatherspace.network.createClient
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.runBlocking
import java.util.*

fun main() {
    val client = createClient(CIO.create())
    runBlocking {
        client.webSocket(method = HttpMethod.Get, path = "/rooms/1") {
            incoming.consumeEach { frame ->

                val receivedMessage = converter!!.deserialize<MessageRequest>(frame)
                println(receivedMessage)

                return@webSocket
            }

            while (true) {
                break
                val othersMessage = receiveDeserialized<MessageRequest>()
                println(othersMessage)
//                val myMessage = Scanner(System.`in`).next()
//                if(myMessage != null) {
//                    send(myMessage)
//                }
            }

//                val myMessage = Scanner(System.`in`).next()
//                if(myMessage != null) {
//                    send(myMessage)
//                }
        }
    }
    client.close()
}