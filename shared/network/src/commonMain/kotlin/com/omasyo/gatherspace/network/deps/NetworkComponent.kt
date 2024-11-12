package com.omasyo.gatherspace.network.deps

import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.network.auth.AuthNetworkSource
import com.omasyo.gatherspace.network.auth.AuthNetworkSourceImpl
import com.omasyo.gatherspace.network.createClient
import com.omasyo.gatherspace.network.message.MessageNetworkSource
import com.omasyo.gatherspace.network.message.MessageNetworkSourceImpl
import com.omasyo.gatherspace.network.room.RoomNetworkSource
import com.omasyo.gatherspace.network.room.RoomNetworkSourceImpl
import com.omasyo.gatherspace.network.user.UserNetworkSource
import com.omasyo.gatherspace.network.user.UserNetworkSourceImpl

class NetworkComponent(tokenStorage: TokenStorage) {
    val authNetworkSource: AuthNetworkSource by lazy {
        AuthNetworkSourceImpl(client)
    }

    val messageNetworkSource: MessageNetworkSource by lazy {
        MessageNetworkSourceImpl(client)
    }

    val roomNetworkSource: RoomNetworkSource by lazy {
        RoomNetworkSourceImpl(client)
    }

    val userNetworkSource: UserNetworkSource by lazy {
        UserNetworkSourceImpl(client)
    }

    private val client by lazy { createClient(tokenStorage = tokenStorage) }
}