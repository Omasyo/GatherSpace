package com.omasyo.gatherspace

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omasyo.gatherspace.auth.LoginRoute
import com.omasyo.gatherspace.auth.SignupRoute
import com.omasyo.gatherspace.home.HomeRoute
import com.omasyo.gatherspace.parcelize.MyParcelable
import com.omasyo.gatherspace.parcelize.MyParcelize
import com.omasyo.gatherspace.profile.ProfileRoute
import kotlinx.serialization.Serializable

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
//        enterTransition = {
//            slideInHorizontally(
//                spec,
//                initialOffsetX = { it }
//            )
//        },
//        exitTransition = {
//            slideOutHorizontally(
//                spec,
//                targetOffsetX = { -it }
//            )
//        },
//        popEnterTransition = {
//            slideInHorizontally(
//                spec,
//                initialOffsetX = { -it }
//            )
//        },
//        popExitTransition = {
//            slideOutHorizontally(
//                spec,
//                targetOffsetX = { it }
//            )
//        }
    ) {
        composable<Signup> {
            SignupRoute(
                onLoginTap = {
                    navController.navigate(Login) {
                        popUpTo(Home)
                    }
                },
                onAuthenticated = {
                    navController.navigate(Home) {

                    }
                },
                onBackTap = { navController.popBackStack() }
            )
        }

        composable<Login> {
            LoginRoute(
                onAuthenticated = { navController.navigate(Home) },
                onSignupTap = {
                    navController.navigate(Signup) {
                        popUpTo(Home)
                    }
                },
                onBackTap = { navController.popBackStack() }
            )
        }

        composable<Home> {
            HomeRoute(
                onLoginTap = { navController.navigate(Login) },
                onProfileTap = { navController.navigate(Profile) },
                onAuthError = {}
            )
        }

        composable<Profile> {
            ProfileRoute(
                onBackTap = { navController.popBackStack() }
            )
        }
    }
}

@Serializable
data object Signup

@Serializable
data object Login

@Serializable
data object Home

@Serializable
data object Profile