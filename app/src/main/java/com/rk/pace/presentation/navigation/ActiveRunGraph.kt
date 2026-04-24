package com.rk.pace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rk.pace.presentation.screens.active_run.ActiveRunViewModel
import com.rk.pace.presentation.screens.active_run.RunScreenRoot
import com.rk.pace.presentation.screens.active_run.SaveRunScreenRoot

fun NavGraphBuilder.activeRunNavGraph(
    navController: NavController
) {

    navigation<Route.Root.ActiveRun>(
        startDestination = Route.ActiveRun.Run
    ) {

        composable<Route.ActiveRun.Run>(
            deepLinks = Route.ActiveRun.Run.deepL
        ) { backStackEntry ->

            val viewModel: ActiveRunViewModel =
                backStackEntry.sharedViewModel(
                    navController,
                    Route.Root.ActiveRun::class
                )

            RunScreenRoot(
                viewModel = viewModel,
                goBack = { navController.popBackStack() },
                goToSaveRun = {
                    navController.navigate(
                        Route.ActiveRun.SaveRun
                    )
                }
            )
        }

        composable<Route.ActiveRun.SaveRun> { backStackEntry ->

            val viewModel: ActiveRunViewModel =
                backStackEntry.sharedViewModel(
                    navController,
                    Route.Root.ActiveRun::class
                )

            SaveRunScreenRoot(
                viewModel = viewModel,
                onBack = {
                    navController.navigate(
                        Route.Root.BotNav
                    ) {
                        popUpTo(Route.Root.ActiveRun) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }
            )
        }

    }

}