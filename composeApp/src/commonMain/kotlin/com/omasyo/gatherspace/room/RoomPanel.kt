package com.omasyo.gatherspace.room

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.itemKey
import com.omasyo.gatherspace.dependencyProvider
import com.omasyo.gatherspace.domain.formatTime
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.models.response.UserDetails
import com.omasyo.gatherspace.ui.components.Image
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.user_placeholder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun RoomPanel(
    modifier: Modifier = Modifier,
    roomId: Int,
    isAuthenticated: Boolean,
    onBackTap: () -> Unit,
    onRegisterTap: () -> Unit,
    onJoin: () -> Unit,
    viewModel: RoomViewModel = dependencyProvider {
        viewModel(key = "room$roomId") {
            RoomViewModel(
                roomId,
                messageRepository,
                roomRepository
            )
        }
    }
) {
    RoomPanel(
        modifier = modifier,
        onBackTap = onBackTap,
        onRegisterTap = onRegisterTap,
        onJoinTap = viewModel::joinRoom,
        isAuthenticated = isAuthenticated,
        message = viewModel.message,
        onMessageChange = viewModel::changeMessage,
        onSendTap = viewModel::sendMessage,
        room = viewModel.room.collectAsStateWithLifecycle().value,
        oldMessages = viewModel.oldMessages.collectAsLazyPagingItems(),
        messages = viewModel.messages,
        onJoin = onJoin,
        onEventReceived = viewModel::onEventReceived,
        state = viewModel.state.collectAsStateWithLifecycle().value,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomPanel(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    onRegisterTap: () -> Unit,
    onJoinTap: () -> Unit,
    isAuthenticated: Boolean,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendTap: () -> Unit,
    room: RoomDetails?,
    oldMessages: LazyPagingItems<Message>,
    messages: List<Message>,
    onJoin: () -> Unit,
    onEventReceived: (RoomEvent) -> Unit,
    state: RoomState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                        Text(room?.name ?: "", maxLines = 1)
                    },
                )
                HorizontalDivider()
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {

            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState,
                reverseLayout = true
            ) {

                items(messages.asReversed(), key = { it.id }) { message ->
                    Message(
                        modifier = Modifier.padding(vertical = 8f.dp, horizontal = 16f.dp).animateItem(),
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
            when {
                !isAuthenticated -> {
                    MessageFieldPlaceholder("Share your thoughts", actionText = "Login", action = onRegisterTap)
                }

                !(room?.isMember ?: false) -> {
                    MessageFieldPlaceholder("Not a member of this room", actionText = "Join?", action = onJoinTap)
                }

                else -> {
                    MessageField(
                        modifier = Modifier.padding(8f.dp),
                        message = message,
                        onMessageChange = onMessageChange,
                        onSendTap = onSendTap,
                    )
                }
            }
        }
    }

    LaunchedEffect(state.event) {
        when (val event = state.event) {

            RoomEvent.JoinedRoom -> onJoin()
            is RoomEvent.Error -> {
                snackbarHostState.showSnackbar(event.message)
            }

            RoomEvent.MessageReceived -> {
                if (listState.firstVisibleItemIndex <= 1) {
                    listState.animateScrollToItem(0)
                }
            }

            else -> Unit
        }
        onEventReceived(state.event)
    }
}

@Composable
private fun MessageField(
    modifier: Modifier = Modifier,
    message: String,
    onMessageChange: (String) -> Unit,
    onSendTap: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
//        IconButton(onClick = { }) {
//            Icon(Icons.Outlined.Image, contentDescription = null)
//        }
        TextField(
            message,
            onValueChange = onMessageChange,
            placeholder = "Message",
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onSend = { onSendTap() }
            ),
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8f.dp)
                .onKeyEvent {
                    when {
                        (it.isCtrlPressed && it.key == Key.Enter && it.type == KeyEventType.KeyUp) -> {
                            onSendTap()
                            true
                        }

                        else -> false
                    }
                },
        )
        IconButton(onClick = onSendTap) {
            Icon(Icons.AutoMirrored.Default.Send, contentDescription = null)
        }
    }
}

@Composable
private fun MessageFieldPlaceholder(
    message: String,
    actionText: String,
    action: () -> Unit,
    modifier: Modifier = Modifier.padding(horizontal = 16f.dp, vertical = 8f.dp),
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4f.dp, Alignment.CenterHorizontally)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        TextButton(onClick = action) {
            Text(actionText)
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

        ) {
            Image(
                message.sender?.imageUrl,
                placeholder = Res.drawable.user_placeholder,
                modifier = Modifier
                    .padding(top = 2f.dp)
                    .size(40f.dp)
                    .clip(MaterialTheme.shapes.small)
            )
//            Image(
//                painterResource(Res.drawable.user_placeholder),
//                null,
//                alignment = Alignment.Center,
//                contentScale = ContentScale.Crop
//            )
        }
        Spacer(Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(message.sender?.username ?: "[deleted]", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(8.dp))
                Text(
                    message.created.formatTime(),
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
    GatherSpaceTheme {

        RoomPanel(
            onBackTap = {},
            message = "Lorem Ipsum",
            onMessageChange = {},
            onRegisterTap = {},
            onJoinTap = {},
            isAuthenticated = true,
            onSendTap = {},
            room = RoomDetails(
                id = 8697, name = "Caleb Wolfe", members = listOf(), created = date, modified = date,
                imageUrl = "", isMember = true,
                creator = UserDetails(
                    id = 7788,
                    username = "Allison Davis",
                    imageUrl = null,
                    created = LocalDateTime(1, 1, 1, 1, 1),
                    modified = LocalDateTime(1, 1, 1, 1, 1)
                )
            ),
            oldMessages = fakeDataFlow.collectAsLazyPagingItems(),
            messages = List(3) {
                Message(
                    id = it,
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    sender = User(id = 5019, username = "Will Rutledge", imageUrl = null),
                    roomId = 2479,
                    created = date,
                    modified = date
                )
            },
            onJoin = {},
            onEventReceived = {},
            state = RoomState.Initial
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