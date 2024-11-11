package com.omasyo.gatherspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.*
import kotlinx.datetime.format.byUnicodePattern
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.itemKey

@Composable
fun MessagePanel(
    modifier: Modifier = Modifier,
    roomId: Int,
    onBackTap: () -> Unit,
    viewModel: RoomViewModel = viewModel(key = "room$roomId") {
        RoomViewModel(
            roomId,
            messageRepository,
            roomRepository
        )
    }
) {
    MessagePanel(
        modifier = modifier,
        onBackTap = onBackTap,
        message = viewModel.message,
        onMessageChange = viewModel::changeMessage,
        onSendTap = viewModel::sendMessage,
        room = viewModel.room.collectAsStateWithLifecycle().value,
        oldMessages = viewModel.oldMessages.collectAsLazyPagingItems(),
        messages = viewModel.messages
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessagePanel(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendTap: () -> Unit,
    room: UiState<RoomDetails>,
    oldMessages: LazyPagingItems<Message>,
    messages: List<Message>,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBackTap) {
                            Icon(
                                Icons.AutoMirrored.Default.ArrowBack,
                                null
                            )
                        }
                    },
                    title = {
                        when (room) {
                            is UiState.Error -> {}
                            UiState.Loading -> {}
                            is UiState.Success -> Text(room.data.name)
                        }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Menu, contentDescription = null)
                        }
                        IconButton(onClick = {}) {
                            Icon(Icons.Filled.Person, contentDescription = null)
                        }
                    }
                )
                HorizontalDivider()
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            val listState = rememberLazyListState()

            LaunchedEffect(messages) {
                listState.scrollToItem(0)
            }

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                reverseLayout = true
            ) {

                items(messages.asReversed(), key = { it.id }) { message ->
                    Message(
                        modifier = Modifier.padding(vertical = 8f.dp, horizontal = 16f.dp),
                        message = message,
                    )
                }
                items(oldMessages.itemCount, key = oldMessages.itemKey { it.id }) {
                    Message(
                        modifier = Modifier.padding(vertical = 8f.dp, horizontal = 16f.dp),
                        message = oldMessages[it]!!,
                    )
                }
            }
            HorizontalDivider()
            Row(
                modifier = Modifier.padding(vertical = 4f.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onSendTap) {
                    Icon(Icons.Default.AccountBox, contentDescription = null)
                }
                TextField(
                    message,
                    onValueChange = onMessageChange,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onSendTap) {
                    Icon(Icons.Default.AccountBox, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun Message(
    modifier: Modifier = Modifier,
    message: Message
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(40f.dp)
                .background(Color.Red)
        )
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(message.sender!!.username, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(8.dp))
                Text(
                    message.created.format(
                        kotlinx.datetime.LocalDateTime.Format {
                            byUnicodePattern("MM dd - HH:mm")
                        }
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                message.content
            )
        }
    }
}


//@RequiresApi(Build.VERSION_CODES.O)
val date = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        MessagePanel(
            onBackTap = {},
            message = "Message",
            onMessageChange = {},
            onSendTap = {},
            room = UiState.Success(
                RoomDetails(
                    id = 8697, name = "Caleb Wolfe", members = listOf(), created = date, modified = date

                )
            ),
            oldMessages = fakeDataFlow.collectAsLazyPagingItems(),
            messages = List(3) {
                Message(
                    id = it,
                    content = "molestie",
                    sender = User(id = 5019, username = "Will Rutledge", imageUrl = null),
                    roomId = 2479,
                    created = date,
                    modified = date
                )
            }
        )
    }
}


val fakeData = List(10) {
    Message(
        id = it + 3, content = "molestie", sender = User(
            id = 9558,
            username = "Marietta Lyons",
            imageUrl = null
        ), roomId = 2479, created = date, modified = date
    )
}

val pagingData = PagingData.from(fakeData)

val fakeDataFlow = MutableStateFlow(pagingData)