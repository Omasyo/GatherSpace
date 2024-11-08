package com.omasyo.gatherspace.ui.room

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.models.response.User
import com.omasyo.gatherspace.ui.components.PTextField
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.datetime.*
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.byUnicodePattern
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    onSendTap: () -> Unit,
    room: RoomDetails,
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
                    title = { Text(room.name) },
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
                items(oldMessages.itemCount, key = {
                    oldMessages[it]!!.id
                }) {
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
                PTextField(
                    "Messenger",
                    onValueChange = {},
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
                Text(message.senderId!!.username, style = MaterialTheme.typography.titleMedium)
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun Preview() {
    GatherSpaceTheme {
        RoomScreen(
            onBackTap = {},
            onSendTap = {},
            room = RoomDetails(
                id = 8697, name = "Caleb Wolfe", members = listOf(), created = date, modified = date

            ),
            oldMessages = fakeDataFlow.collectAsLazyPagingItems(),
            messages = List(3) {
                Message(
                    id = it,
                    content = "molestie",
                    senderId = User(id = 5019, username = "Will Rutledge"),
                    roomId = 2479,
                    created = date,
                    modified = date
                )
            }
        )
    }
}


@RequiresApi(Build.VERSION_CODES.O)
val fakeData = List(10) {
    Message(
        id = it + 3, content = "molestie", senderId = User(
            id = 9558,
            username = "Marietta Lyons"
        ), roomId = 2479, created = date, modified = date
    )
}

// create pagingData from a list of fake data
@RequiresApi(Build.VERSION_CODES.O)
val pagingData = PagingData.from(fakeData)

// pass pagingData containing fake data to a MutableStateFlow
@RequiresApi(Build.VERSION_CODES.O)
val fakeDataFlow = MutableStateFlow(pagingData)