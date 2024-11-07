package com.omasyo.gatherspace.ui.room

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.models.response.Message
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.ui.components.PTextField
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(
    modifier: Modifier = Modifier,
    onBackTap: () -> Unit,
    onSendTap: () -> Unit,
    room: RoomDetails,
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
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(messages, key = { it.id }) { message ->
                    Message(
                        modifier = Modifier.padding(vertical = 8f.dp, horizontal = 16f.dp)
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
                Text("John Bosco", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.width(8.dp))
                Text(
                    "12:35",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                "For all fo us that want to go the house please forgive my insolence haha, No I wont choose to do that.\n\n" +
                        "Please forgive my stupidity, I've been applying for jobs but as you know no luck on that angle"
            )
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private val date = LocalDateTime.now().toKotlinLocalDateTime()

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
            messages = List(40) {
                Message(id = it, content = "molestie", senderId = null, roomId = 2479, created = date, modified = date)
            }
        )
    }
}