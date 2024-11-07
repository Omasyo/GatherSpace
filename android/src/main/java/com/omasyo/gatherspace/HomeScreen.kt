package com.omasyo.gatherspace

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.models.response.RoomDetails
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

sealed interface RoomState {
    data object Loading : RoomState
    data class Error(val reason: String) : RoomState
    data class Success(val rooms: List<Room>) : RoomState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onRoomTap: (Int) -> Unit,
    state: RoomState
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { }) { Icon(Icons.Default.Menu, null) } },
                title = { Text("GatherSpace") })
        },
    ) { innerPadding ->
        when (state) {
            is RoomState.Error -> ErrorPlaceholder(
                modifier = Modifier.padding(innerPadding),
                reason = state.reason,
                onRetry = {}
            )

            RoomState.Loading -> LoadingPlaceholder(Modifier.padding(innerPadding))
            is RoomState.Success -> {
                LazyColumn(
                    Modifier.padding(innerPadding)
                ) {
                    items(state.rooms, key = { it.id }) { room ->
                        with(room) {
                            RoomTile(
                                name = name,
                                onTap = {onRoomTap(room.id)},
                                onListenTap = {}
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun RoomTile(
    modifier: Modifier = Modifier,
    name: String,
    onTap: () -> Unit,
    onListenTap: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = onTap)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.Red)
        )
        Spacer(Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(name)
        }
    }
}

@Composable
private fun ErrorPlaceholder(
    modifier: Modifier = Modifier,
    reason: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(reason, style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text("Retry")
        }
    }
}

@Composable
private fun LoadingPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        repeat(20) { index ->
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                )
                Spacer(Modifier.width(16.dp))
                Box(Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(if (index % 2 == 0) 220.dp else 192.dp, 24f.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
            HorizontalDivider()
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
        HomeScreen(onRoomTap = {}, state = RoomState.Success(rooms))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private val rooms = List(10) {
    Room(id = it, name = "Maribel Gaines")
}