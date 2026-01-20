package com.rk.pace.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.rk.pace.theme.notifications
import com.rk.pace.theme.run
import com.rk.pace.theme.stats
import com.rk.pace.theme.user

data class BotNav(
    val route: Route,
    val i: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotNavScreen(
    navController: NavController
) {
    val topNavController = rememberNavController()
    val navBackStackEntry by topNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val botNavList = listOf(
        BotNav(
            route = Route.BotNav.Feed,
            i = feed
        ),
        BotNav(
            route = Route.ActiveRun.Run,
            i = run
        ),
        BotNav(
            route = Route.BotNav.Stats,
            i = stats
        ),
        BotNav(
            route = Route.BotNav.MyProfile,
            i = user
        )
    )

    val title = when {
        currentDestination?.hasRoute(Route.BotNav.Feed::class) == true -> "FEED"
        currentDestination?.hasRoute(Route.BotNav.Stats::class) == true -> "STATS"
        currentDestination?.hasRoute(Route.BotNav.MyProfile::class) == true -> "PROFILE"
        else -> ""
    }

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 15.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 5.sp
                    )
                },
                actions = {
                    if (currentDestination?.hasRoute(Route.BotNav.Feed::class) == true) {
                        IconButton(
                            onClick = {
                                navController.navigate(Route.Root.Search)
                            }
                        ) {
                            Icon(
                                imageVector = add_people,
                                contentDescription = ""
                            )
                        }
                        IconButton(
                            onClick = {

                            } // go to notifications screen
                        ) {
                            Icon(
                                imageVector = notifications,
                                contentDescription = ""
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                botNavList.forEach {
                    NavigationBarItem(
                        onClick = {
                            if (it.route == Route.ActiveRun.Run) {
                                navController.navigate(it.route)
                            } else {
                                topNavController.navigate(it.route) {
                                    popUpTo(Route.BotNav.Feed) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = it.i,
                                contentDescription = ""
                            )
                        },
                        selected = currentDestination?.hasRoute(it.route::class) == true
                    )
                }
            }
        }
    ) {
        NavHost(
            navController = topNavController,
            startDestination = Route.BotNav.Feed,
            modifier = Modifier.padding(it)
        ) {
            composable<Route.BotNav.Feed> {
                FeedScreen(
                    goToRunStatsScreen = { runId ->
                        navController.navigate(
                            Route.Root.RunStats(
                                runId = runId
                            )
                        )
                    }
                )
            }
            composable<Route.BotNav.Stats> {
                StatsScreen()
            }
            composable<Route.BotNav.MyProfile> {
                UserScreen(
//                    onSignOut = {
//                        navController.navigate(Route.Root.Auth)
//                    }
                )
            }
        }
    }
}