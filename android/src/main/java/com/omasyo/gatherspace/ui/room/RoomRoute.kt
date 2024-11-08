package com.omasyo.gatherspace.ui.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.gatherspace.domain.message.MessageRepository
import com.omasyo.gatherspace.domain.message.MessageRepositoryImpl
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.network.message.createMessageNetworkSource
import kotlinx.coroutines.Dispatchers

val messageRepository = MessageRepositoryImpl(createMessageNetworkSource(), Dispatchers.IO)

@Composable
fun RoomRoute(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    onSendTap: () -> Unit,
    viewModel: RoomViewModel = viewModel { RoomViewModel(createSavedStateHandle(), messageRepository) },
) {
    RoomScreen(
        modifier = modifier,
        onBackTap = onBackTap,
        onSendTap = onSendTap,
        room = RoomDetails(id = 5296, name = "Lorrie Delgado", members = listOf(), created = date, modified = date),
        oldMessages = viewModel.oldMessages.collectAsLazyPagingItems(),
        messages = viewModel.messages.toList()
    )
}