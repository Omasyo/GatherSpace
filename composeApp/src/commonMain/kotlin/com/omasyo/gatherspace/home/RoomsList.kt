package com.omasyo.gatherspace.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.ui.theme.GatherSpaceTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.ui.components.ErrorPlaceholder
import com.omasyo.gatherspace.ui.components.Image
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.room_placeholder

@Composable
fun RoomsList(
    modifier: Modifier = Modifier,
    onRoomTap: (Int) -> Unit,
    onRetry: () -> Unit,
    state: UiState<List<Room>>
) {
    when (state) {
        is UiState.Error -> ErrorPlaceholder(modifier, message = state.reason, onRetry = onRetry)
        UiState.Loading -> LoadingPlaceholder(modifier)

        is UiState.Success -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(vertical = 24f.dp),
        ) {
            items(state.data, key = { it.id }) { room ->
                Room(
                    room = room,
                    modifier = Modifier
                        .clickable { onRoomTap(room.id) }
                        .padding(horizontal = 16f.dp, vertical = 8f.dp)
                )
            }
        }

    }
}

@Composable
private fun Room(
    modifier: Modifier = Modifier,
    room: Room,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8f.dp)
    ) {
        Image(
            imageUrl = "TODO",
            placeholder = Res.drawable.room_placeholder,
            modifier = Modifier.size(48f.dp)
                .clip(MaterialTheme.shapes.small),
        )
        Text(
            text = room.name,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun LoadingPlaceholder(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(horizontal = 16f.dp, vertical = 24f.dp),
        verticalArrangement = Arrangement.spacedBy(16f.dp),
    ) {
        repeat(20) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8f.dp)
            ) {
                Box(
                    modifier = Modifier.size(48f.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
                Box(
                    modifier = Modifier.size(220f.dp, 20f.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }
        }
    }
}

@Preview
@Composable
private fun RoomsListPreview() {
    GatherSpaceTheme(false) {
        RoomsList(
            onRoomTap = {},
            onRetry = {},
            state = UiState.Error("An Error Occurreth")
        )
    }
}