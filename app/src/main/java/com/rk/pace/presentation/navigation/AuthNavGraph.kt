package com.rk.pace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rk.pace.auth.presentation.AuthScreen

fun NavGraphBuilder.authNavGraph(
    navController: NavController
) {

    composable<Route.Root.Auth> {
        AuthScreen(navController = navController)
    }

}