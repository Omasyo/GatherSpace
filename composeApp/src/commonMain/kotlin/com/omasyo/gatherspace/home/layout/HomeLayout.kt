package com.omasyo.gatherspace.home.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.ThreePaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.omasyo.gatherspace.BackHandler
import com.omasyo.gatherspace.CreateRoom
import com.omasyo.gatherspace.HomeRoutes
import com.omasyo.gatherspace.RoomRoute


@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(
    modifier: Modifier = Modifier,
    isAuthenticated: Boolean,
    topBar: @Composable (Boolean) -> Unit,
    roomsList: @Composable () -> Unit,
    roomsGrid: @Composable () -> Unit,
    roomView: @Composable (roomId: Int) -> Unit,
    createRoomView: @Composable () -> Unit,
    loginPlaceholder: @Composable () -> Unit,
    navigator: ThreePaneScaffoldNavigator<HomeRoutes>,
) {
    BackHandler(enabled = navigator.canNavigateBack(), onBack = { navigator.navigateBack() })

    val borderColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strokeWidth = 1f.dp

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane(
                Modifier
                    .drawBehind {
                        drawLine(
                            borderColor,
                            Offset(size.width, 0f),
                            Offset(size.width, size.height),
                            strokeWidth.toPx()
                        )
                    }) {
                if (navigator.scaffoldValue.primary == PaneAdaptedValue.Hidden) {
                    Column(Modifier.fillMaxSize()) {
                        Box(
                            Modifier
                                .heightIn(max = 64f.dp)
                                .drawBehind {
                                    drawLine(
                                        borderColor,
                                        Offset(0f, size.height),
                                        Offset(size.width, size.height),
                                        strokeWidth.toPx()
                                    )
                                }
                        ) {
                            topBar(false)
                        }
                        roomsGrid()
                    }
                } else {
                    if (isAuthenticated) {
                        roomsList()
                    } else {
                        loginPlaceholder()
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                val route = navigator.currentDestination?.content
                when (route) {
                    CreateRoom -> createRoomView()

                    is RoomRoute -> roomView(route.id)

                    null -> {
                        if (!scaffoldStateTransition.isRunning) {
                            Column {
                                Box(
                                    Modifier
                                        .heightIn(max = 64f.dp)
//                                    .fillMaxWidth()
                                        .drawBehind {
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
                                roomsGrid()
                            }
                        }
                    }
                }
            }
        },
        extraPane = {
            AnimatedPane(
                Modifier
                    .drawBehind {
                        drawLine(
                            borderColor,
                            Offset(0f, 0f),
                            Offset(0f, size.height),
                            strokeWidth.toPx()
                        )
                    }) {
                //todo maybe we add extras like room details/user details
            }
        },
    )
}