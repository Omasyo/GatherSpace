package com.omasyo.gatherspace

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.omasyo.gatherspace.domain.formatTime
import com.omasyo.gatherspace.home.UiState
import com.omasyo.gatherspace.home.fakeDataFlow
import com.omasyo.gatherspace.ui.components.TextField
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.user_placeholder
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.imageResource

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
//                    actions = {
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Outlined.Call, contentDescription = null)
//                        }
//                        IconButton(onClick = {}) {
//                            Icon(Icons.Outlined.Info, contentDescription = null)
//                        }
//                    }
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
                IconButton(onClick = { }) {
                    Icon(Icons.Outlined.Image, contentDescription = null)
                }
                TextField(
                    message,
                    onValueChange = onMessageChange,
                    placeholder = "Message",
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8f.dp, vertical = 8f.dp),
                )
                IconButton(onClick = onSendTap) {
                    Icon(Icons.AutoMirrored.Default.Send, contentDescription = null)
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
                .padding(top = 2f.dp)
                .size(40f.dp)
                .clip(MaterialTheme.shapes.small)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(
                        imageResource(Res.drawable.user_placeholder)
//                        "https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Flag_of_Nigeria.svg/255px-Flag_of_Nigeria.svg.png"
                    )
                    .crossfade(true).build(),
                null
            )
        }
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
    MaterialTheme {
        var message by remember { mutableStateOf("") }
        MessagePanel(
            onBackTap = {},
            message = message,
            onMessageChange = { message = it },
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
                    content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
                    sender = User(id = 5019, username = "Will Rutledge", imageUrl = null),
                    roomId = 2479,
                    created = date,
                    modified = date
                )
            }
        )
    }
}