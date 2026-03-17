package com.rk.pace.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.rk.pace.auth.presentation.AuthScreen
import com.rk.pace.presentation.screens.active_run.ActiveRunViewModel
import com.rk.pace.presentation.screens.active_run.RunScreen
import com.rk.pace.presentation.screens.active_run.SaveRunScreen
import com.rk.pace.presentation.screens.connections.ConnectionsScreen
import com.rk.pace.presentation.screens.my_profile.EditProfileScreen
import com.rk.pace.presentation.screens.run_stats.RunStatsScreen
import com.rk.pace.presentation.screens.search.SearchScreen
import com.rk.pace.presentation.screens.user_profile.UserProfileScreen
import com.rk.pace.presentation.ut.defaultEnterTransition
import com.rk.pace.presentation.ut.defaultExitTransition
import com.rk.pace.presentation.ut.defaultPopEnterTransition
import com.rk.pace.presentation.ut.defaultPopExitTransition

@Composable
fun NavGraph(
    startDestination: Route
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = { defaultEnterTransition() },
        exitTransition = { defaultExitTransition() },
        popEnterTransition = { defaultPopEnterTransition() },
        popExitTransition = { defaultPopExitTransition() }
    ) {

        composable<Route.Root.Auth> {
            AuthScreen(
                navController = navController
            )
        }

        composable<Route.Root.BotNav> {
            BotNavScreen(
                navController = navController
            )
        }

        navigation<Route.Root.ActiveRun>(
            startDestination = Route.ActiveRun.Run
        ) {
            composable<Route.ActiveRun.Run>(
                deepLinks = Route.ActiveRun.Run.deepL
            ) { backStackEntry ->
                val entry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.Root.ActiveRun)
                }

                val viewModel: ActiveRunViewModel =
                    hiltViewModel(entry)

                RunScreen(
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
                val entry = remember(backStackEntry) {
                    navController.getBackStackEntry(Route.Root.ActiveRun)
                }

                val viewModel: ActiveRunViewModel =
                    hiltViewModel(entry)

                SaveRunScreen(
                    viewModel = viewModel,
                    goBack = {
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

        composable<Route.Root.RunStats> {
            RunStatsScreen(
                goBack = { navController.popBackStack() }
            )
        }

        composable<Route.Root.Search> {
            SearchScreen(
                onUserClick = { userId ->
                    navController.navigate(
                        Route.Root.UserProfile(userId)
                    )
                },
                goBack = { navController.popBackStack() }
            )
        }

        composable<Route.Root.EditProfile> {
            EditProfileScreen(
                goBack = { navController.popBackStack() }
            )
        }

        composable<Route.Root.UserProfile> {
            UserProfileScreen(
                onRunClick = { userId, runId ->
                    navController.navigate(
                        Route.Root.RunStats(
                            userId,
                            runId
                        )
                    )
                },
                onFollowersClick = { userId, tab ->
                    navController.navigate(
                        Route.Root.Connections(
                            userId = userId,
                            tab = tab
                        )
                    )
                },
                onFollowingClick = { userId, tab ->
                    navController.navigate(
                        Route.Root.Connections(
                            userId = userId,
                            tab = tab
                        )
                    )
                },
                goBack = {
                    navController.popBackStack()
                }
            )
        }

        composable<Route.Root.Connections> {
            ConnectionsScreen(
                goBack = {
                    navController.popBackStack()
                },
                onUserClick = { userId ->
                    navController.navigate(
                        Route.Root.UserProfile(userId)
                    )
                }
            )
        }
    }
}