package com.rk.pace.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rk.pace.presentation.screens.connections.ConnectionsScreen
import com.rk.pace.presentation.screens.my_profile.EditProfileScreen
import com.rk.pace.presentation.screens.run_stats.RunStatsScreen
import com.rk.pace.presentation.screens.search.SearchScreen
import com.rk.pace.presentation.screens.stats.AddGoalScreen
import com.rk.pace.presentation.screens.user_profile.UserProfileScreen

fun NavGraphBuilder.rootNavGraph(
    navController: NavController
) {

    composable<Route.Root.AddGoal> {
        AddGoalScreen(
            goBack = { navController.popBackStack() }
        )
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