package com.rk.pace.presentation.screens.top

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rk.pace.presentation.Route
import com.rk.pace.presentation.screens.home.HomeScreen
import com.rk.pace.presentation.screens.you.YouScreen

data class BottomNavItems(
    val route: Route,
    val icon: ImageVector
)

val bottomNavItemsList = listOf(
    BottomNavItems(
        route = Route.Top.Home,
        icon = Icons.Default.Home
    ),
//    BottomNavItems(
//        route = Route.Root.Run,
//        icon = Icons.AutoMirrored.Filled.DirectionsRun
//    ),
    BottomNavItems(
        route = Route.Top.You,
        icon = Icons.Default.Person
    )
)

@Composable
fun TopScreen(
    navController: NavController
) {
    val topNavController = rememberNavController()
    val navBackStackEntry by topNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItemsList.forEach { item ->
                    NavigationBarItem(
                        onClick = {
                            topNavController.navigate(item.route) {
                                popUpTo(Route.Top.Home) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = ""
                            )
                        },
                        selected = currentDestination?.hasRoute(item.route::class) == true
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.Root.Run)
                },
                modifier = Modifier
                    .offset(y = 40.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                    contentDescription = ""
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { it ->
        NavHost(
            navController = topNavController,
            startDestination = Route.Top.Home,
            modifier = Modifier.padding(it)
        ) {
            composable<Route.Top.Home> {
                HomeScreen()
            }
            composable<Route.Top.You> {
                YouScreen()
            }
        }
    }
}