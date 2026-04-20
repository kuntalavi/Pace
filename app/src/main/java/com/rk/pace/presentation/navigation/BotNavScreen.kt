package com.rk.pace.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rk.pace.presentation.screens.feed.FeedScreen
import com.rk.pace.presentation.screens.my_profile.UserScreen
import com.rk.pace.presentation.screens.stats.StatsScreen
import com.rk.pace.theme.add_people
import com.rk.pace.theme.feed
import com.rk.pace.theme.progress
import com.rk.pace.theme.run
import com.rk.pace.theme.user

data class BotNav(
    val route: Route,
    val icon: ImageVector,
    val iconSelected: ImageVector
)

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotNavScreen(
    navController: NavController
) {
    val tNavController = rememberNavController()
    val navBackStackEntry by tNavController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val botNavList = listOf(
        BotNav(
            route = Route.BotNav.Feed,
            icon = feed,
            iconSelected = feed
        ),
        BotNav(
            route = Route.ActiveRun.Run,
            icon = run,
            iconSelected = run
        ),
        BotNav(
            route = Route.BotNav.Stats,
            icon = progress,
            iconSelected = progress
        ),
        BotNav(
            route = Route.BotNav.MyProfile,
            icon = user,
            iconSelected = user
        )
    )

    val title by remember(destination) {
        derivedStateOf {
            when {
                destination?.hasRoute<Route.BotNav.Feed>() ?: false -> "FEED"
                destination?.hasRoute<Route.BotNav.Stats>() ?: false -> "STATS"
                destination?.hasRoute<Route.BotNav.MyProfile>() ?: false -> "PROFILE"
                else -> ""
            }
        }
    }

    val feedScreen = destination?.hasRoute<Route.BotNav.Feed>() ?: false

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background
                ),
                title = {
                    Text(
                        text = title,
                        modifier = Modifier
                            .padding(start = 5.dp),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 5.sp
                    )
                },
                navigationIcon = { },
                actions = {
                    if (feedScreen) {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    Route.Root.Search
                                )
                            }
                        ) {
                            Icon(
                                imageVector = add_people,
                                contentDescription = ""
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            PaceBotNavBar(
                botNavList = botNavList,
                destination = destination,
                onTabClick = { route ->
                    if (route == Route.ActiveRun.Run) {
                        navController.navigate(
                            Route.ActiveRun.Run
                        )
                    } else {
                        tNavController.navigate(
                            route
                        ) {
                            popUpTo(
                                Route.BotNav.Feed
                            ) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) {
        NavHost(
            navController = tNavController,
            startDestination = Route.BotNav.Feed,
            modifier = Modifier.padding(it)
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
}