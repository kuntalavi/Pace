package com.rk.pace.presentation.navigation

import androidx.compose.runtime.State
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rk.pace.presentation.screens.feed.FeedScreenRoot
import com.rk.pace.presentation.screens.my_profile.UserScreenRoot
import com.rk.pace.presentation.screens.stats.StatsScreen

fun NavGraphBuilder.botNavGraph(
    navController: NavController,
    reload: State<Long>
) {

    navigation<Route.Root.BotNav>(
        startDestination = Route.BotNav.Feed
    ) {

        composable<Route.BotNav.Feed> {
            FeedScreenRoot(
                reload = reload.value,
                onRunClick = { userId, runId ->
                    navController.navigate(
                        Route.Root.RunStats(
                            userId,
                            runId
                        )
                    )
                },
                onUserClick = { userId ->
                    navController.navigate(
                        Route.Root.UserProfile(
                            userId
                        )
                    )
                }
            )
        }

        composable<Route.BotNav.Stats> {
            StatsScreen(
                onAddGoalClick = {
                    navController.navigate(
                        Route.Root.AddGoal
                    )
                },
                onGoalClick = { }
            )
        }

        composable<Route.BotNav.MyProfile> {
            UserScreenRoot(
                onEditClick = {
                    navController.navigate(Route.Root.EditProfile)
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
                onRunClick = { userId, runId ->
                    navController.navigate(
                        Route.Root.RunStats(
                            userId,
                            runId
                        )
                    )

                }
            )
        }

    }

}