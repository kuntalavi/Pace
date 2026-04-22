package com.rk.pace.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.rk.pace.presentation.screens.feed.FeedScreen
import com.rk.pace.presentation.screens.my_profile.UserScreen
import com.rk.pace.presentation.screens.stats.StatsScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.botNavGraph(
    navController: NavController
) {

    navigation<Route.Root.BotNav>(
        startDestination = Route.BotNav.Feed
    ) {

        composable<Route.BotNav.Feed> {
            FeedScreen(
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
                onGoalClick = {

                }
            )
        }

        composable<Route.BotNav.MyProfile> {
            UserScreen(
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
                }
            )
        }

    }

}