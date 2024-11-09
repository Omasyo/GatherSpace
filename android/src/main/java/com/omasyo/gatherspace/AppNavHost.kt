package com.omasyo.gatherspace

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omasyo.gatherspace.ui.HomeRoute
import com.omasyo.gatherspace.ui.room.RoomRoute
import kotlinx.serialization.Serializable


interface NavRoute {
    val route: String
}

private val spec: FiniteAnimationSpec<IntOffset> =
    spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController,
        startDestination = Home,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                spec,
                initialOffsetX = { it }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                spec,
                targetOffsetX = { -it }
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                spec,
                initialOffsetX = { -it }
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                spec,
                targetOffsetX = { it }
            )
        }
    ) {
        composable<Home> {
            HomeRoute(
                onRoomTap = {
                    navController.navigate(Room(1))
                },
                onCreateRoom = {}
            )
        }

        composable<Room> {
            RoomRoute(
                onBackTap = {},
                onSendTap = {}
            )
        }
    }
}

@Serializable
data object Home

@Serializable
data class Room(val id: Int)