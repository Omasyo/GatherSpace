package com.omasyo.gatherspace

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.omasyo.gatherspace.auth.LoginRoute
import com.omasyo.gatherspace.auth.SignupRoute
import com.omasyo.gatherspace.home.HomeRoute
import com.omasyo.gatherspace.profile.ProfileRoute
import kotlinx.serialization.Serializable

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
                initialOffsetX = { it }
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }
            )
        }
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