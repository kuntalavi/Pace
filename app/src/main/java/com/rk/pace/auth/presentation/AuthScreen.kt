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
import com.rk.pace.auth.presentation.screens.WelcomeScreen
import com.rk.pace.presentation.Route

@Composable
fun AuthScreen(
    navController: NavController
) {
    val authNavController = rememberNavController()

    Scaffold() {
        NavHost(
            navController = authNavController,
            startDestination = Route.Auth.Welcome,
            modifier = Modifier.padding(it)
        ) {

            composable<Route.Auth.Welcome>{
                WelcomeScreen(
                    goToSignUp = {
                        authNavController.navigate(Route.Auth.SignUp)
                    },
                    goToSignIn = {
                        authNavController.navigate(Route.Auth.SignIn)
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
}