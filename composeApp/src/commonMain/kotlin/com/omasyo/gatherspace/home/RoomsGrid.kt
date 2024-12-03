package com.omasyo.gatherspace.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.UiState
import com.omasyo.gatherspace.models.response.Room
import com.omasyo.gatherspace.ui.components.ErrorPlaceholder
import com.omasyo.gatherspace.ui.components.Image
import gatherspace.composeapp.generated.resources.Res
import gatherspace.composeapp.generated.resources.room_placeholder

@Composable
fun RoomsGrid(
    modifier: Modifier = Modifier,
    onRoomTap: (Int) -> Unit,
    onRetry: () -> Unit,
    state: UiState<List<Room>>
) {
    when (state) {
        is UiState.Error -> ErrorPlaceholder(modifier, message = state.reason, onRetry = onRetry)
        UiState.Loading -> LoadingPlaceholder(modifier)

        is UiState.Success -> BoxWithConstraints {
            LazyVerticalGrid(
                modifier = modifier,
                columns = GridCells.Adaptive(if (maxWidth < 480f.dp) 120f.dp else 200f.dp),
                contentPadding = PaddingValues(vertical = 24f.dp, horizontal = 16f.dp),
                verticalArrangement = Arrangement.spacedBy(16f.dp),
                horizontalArrangement = Arrangement.spacedBy(16f.dp)
            ) {
                items(state.data, key = { it.id }) { room ->
                    Room(
                        room = room,
                        modifier = Modifier
                            .clickable { onRoomTap(room.id) },
                        expanded = maxWidth >= 480f.dp
                    )
                }
            }
        }

    }
}

@Composable
private fun Room(
    modifier: Modifier = Modifier,
    room: Room,
    expanded: Boolean,
) {
    Column(
        modifier
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
    ) {
        Image(
            imageUrl = room.imageUrl,
            placeholder = Res.drawable.room_placeholder,
            modifier = Modifier
                .weight(1f)
        )
        Box(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.End,
                minLines = if (expanded) 2 else 1,
                maxLines = if (expanded) 2 else 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(16f.dp)
            )
        }
    }
}

@Composable
private fun LoadingPlaceholder(
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(120f.dp),
        contentPadding = PaddingValues(vertical = 24f.dp, horizontal = 16f.dp),
        verticalArrangement = Arrangement.spacedBy(16f.dp),
        horizontalArrangement = Arrangement.spacedBy(16f.dp)
    ) {
        items(20) {
            Box(
                modifier
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}