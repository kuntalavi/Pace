package com.rk.pace.auth.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rk.pace.auth.presentation.screens.SignInScreen
import com.rk.pace.auth.presentation.screens.SignUpScreen
import com.rk.pace.presentation.Route

@Composable
fun AuthScreen(
    navController: NavController
) {
    val authNavController = rememberNavController()

    Scaffold() {
        NavHost(
            navController = authNavController,
            startDestination = Route.Auth.SignUp,
            modifier = Modifier.padding(it)
        ) {
            composable<Route.Auth.SignUp> {
                SignUpScreen(
                    goToSignIn = {
                        authNavController.navigate(Route.Auth.SignIn)
                    },
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
                    goToSignUp = {
                        authNavController.navigate(Route.Auth.SignUp)
                    },
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
}