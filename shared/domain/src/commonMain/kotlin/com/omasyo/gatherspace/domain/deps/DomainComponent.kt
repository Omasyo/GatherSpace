package com.omasyo.gatherspace.domain.deps

import com.omasyo.gatherspace.domain.auth.AuthRepository
import com.omasyo.gatherspace.domain.auth.AuthRepositoryImpl
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.message.MessageRepositoryImpl
import com.omasyo.gatherspace.domain.room.RoomRepository
import com.omasyo.gatherspace.domain.room.RoomRepositoryImpl
import com.omasyo.gatherspace.domain.user.UserRepository
import com.omasyo.gatherspace.domain.user.UserRepositoryImpl
import com.omasyo.gatherspace.models.TokenStorage
import com.omasyo.gatherspace.network.deps.NetworkComponent
import kotlinx.coroutines.CoroutineDispatcher

abstract class DomainComponent(
    networkComponent: NetworkComponent,
    tokenStorage: TokenStorage,
    dispatcher: CoroutineDispatcher
) {
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(networkComponent.authNetworkSource, tokenStorage, dispatcher)
    }

    val messageRepository: MessageRepository by lazy {
        MessageRepositoryImpl(networkComponent.messageNetworkSource, dispatcher)
    }

    val roomRepository: RoomRepository by lazy {
        RoomRepositoryImpl(networkComponent.roomNetworkSource, dispatcher)
    }

    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(networkComponent.userNetworkSource, dispatcher)
    }
}