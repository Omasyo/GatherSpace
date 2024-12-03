package com.omasyo.gatherspace.createroom

import com.omasyo.gatherspace.TextFieldState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.io.Buffer

interface CreateRoomViewModel {
    val coroutineScope: CoroutineScope

    val state: StateFlow<CreateRoomState>

    val nameField: TextFieldState

    val descriptionField: TextFieldState

    val image: Buffer?

    fun changeName(value: String)

    fun changeDescription(value: String)

    fun updateImage(image: Buffer)

    fun submit()

    fun onEventReceived(event: CreateRoomEvent)
}