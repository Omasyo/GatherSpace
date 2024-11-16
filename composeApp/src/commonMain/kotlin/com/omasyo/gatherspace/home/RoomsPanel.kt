package com.omasyo.gatherspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.models.response.Room
import androidx.compose.desktop.ui.tooling.preview.Preview
import com.omasyo.gatherspace.ui.components.ErrorPlaceholder

@Composable
fun RoomsView(
    modifier: Modifier = Modifier,
    onRoomTap: (Room) -> Unit,
    onRetry: () -> Unit,
    state: UiState<List<Room>>
) {
    when (state) {
        is UiState.Loading -> LoadingPlaceholder(modifier)
        is UiState.Error -> ErrorPlaceholder(modifier, state.reason, onRetry)
        is UiState.Success -> RoomsList(modifier, state.data, onRoomTap)
    }
}

@Composable
private fun RoomsList(
    modifier: Modifier,
    rooms: List<Room>,
    onRoomTap: (Room) -> Unit
) {
    LazyColumn(modifier) {
        items(rooms, key = { it.id }) { room ->
            RoomTile(room = room, onTap = { onRoomTap(room) })
        }
    }
}


@Composable
fun RoomTile(
    modifier: Modifier = Modifier,
    room: Room,
    onTap: () -> Unit,
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
            Text(room.name)
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

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        RoomsView(
            onRoomTap = {},
            onRetry = {},
            state = UiState.Success(rooms)
        )
    }
}

private val rooms = List(10) {
    Room(id = it, name = "Maribel Ganes")
}