package com.rk.pace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rk.pace.auth.presentation.screens.SignInScreen
import com.rk.pace.auth.presentation.screens.SignUpScreen
import com.rk.pace.auth.presentation.screens.WelcomeScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController
) {

    navigation<Route.Root.Auth>(
        startDestination = Route.Auth.Welcome
    ) {

        composable<Route.Auth.Welcome> {
            WelcomeScreen(
                onSignUpClick = {
                    navController.navigate(Route.Auth.SignUp)
                },
                onSignInClick = {
                    navController.navigate(Route.Auth.SignIn)
                }
            )
        }

        composable<Route.Auth.SignUp> {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Route.Root.BotNav) {
                        popUpTo(Route.Root.Auth) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Route.Auth.SignIn> {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Route.Root.BotNav) {
                        popUpTo(Route.Root.Auth) {
                            inclusive = true
                        }
                    }
                }
            )
        }

    }

}