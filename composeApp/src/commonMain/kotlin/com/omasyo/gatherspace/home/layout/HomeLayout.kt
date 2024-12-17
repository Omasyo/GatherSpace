package com.omasyo.gatherspace.home.layout

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import com.omasyo.gatherspace.BackHandler

sealed interface HomeLayoutView {
    data object None : HomeLayoutView
    data object Discover : HomeLayoutView
    data class Room(val id: Int) : HomeLayoutView
    data object CreateRoom : HomeLayoutView
}

@Composable
fun HomeLayout(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    isAuthenticated: Boolean,
    topBar: @Composable (Boolean) -> Unit,
    roomsList: @Composable () -> Unit,
    roomsGrid: @Composable () -> Unit,
    roomView: @Composable (Int) -> Unit,
    createRoomView: @Composable () -> Unit,
    selectedView: HomeLayoutView,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
) {

    BackHandler(enabled = selectedView !is HomeLayoutView.None, onBack = onBack)

    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strokeWidth = 1f.dp

    Row(modifier) {
        Box(
            Modifier.run {
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED)
                    width(400f.dp)
                else this
            }
        ) {
            if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
                roomsList()
            } else {
                Column {
                    AnimatedVisibility(
                        selectedView is HomeLayoutView.None || selectedView is HomeLayoutView.Discover,
                        modifier = Modifier.drawBehind {
                            drawLine(
                                borderColor,
                                Offset(0f, size.height),
                                Offset(size.width, size.height),
                                strokeWidth.toPx()
                            )
                        }) {
                        topBar(false)
                    }
                    AnimatedContent(selectedView, label = "listPane") { selectedView ->

                        when (selectedView) {
                            HomeLayoutView.None -> {
                                Column {
                                    if (isAuthenticated) roomsList() else roomsGrid()
                                }
                            }

                            HomeLayoutView.Discover -> {
                                Column {
                                    roomsGrid()
                                }
                            }

                            is HomeLayoutView.Room -> {
                                roomView(selectedView.id)
                            }

                            HomeLayoutView.CreateRoom -> {
                                createRoomView()
                            }
                        }
                    }
                }
            }

        }
        AnimatedVisibility(
            windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED,
            modifier = Modifier
                .weight(1f)
                .drawBehind {
                    drawLine(
                        borderColor,
                        Offset(0f, 0f),
                        Offset(0f, size.height),
                        strokeWidth.toPx()
                    )
                }
        ) {
            val view = if (selectedView == HomeLayoutView.None) HomeLayoutView.Discover else selectedView
            AnimatedContent(view, label = "detailsPane") { selected ->
                when (selected) {
                    HomeLayoutView.None, HomeLayoutView.Discover -> {
                        Column {
                            Box(modifier = Modifier.drawBehind {
                                drawLine(
                                    borderColor,
                                    Offset(0f, size.height),
                                    Offset(size.width, size.height),
                                    strokeWidth.toPx()
                                )
                            }
                            ) {
                                topBar(true)
                            }
                            Box(Modifier.weight(1f)) {
                                roomsGrid()
                            }
                        }
                    }

                    is HomeLayoutView.Room -> {
                        roomView(selected.id)
                    }

                    HomeLayoutView.CreateRoom -> {
                        createRoomView()
                    }
                }
            }
        }
    }
}