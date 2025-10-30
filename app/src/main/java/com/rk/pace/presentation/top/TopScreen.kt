package com.rk.pace.presentation.top

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rk.pace.presentation.Route
import com.rk.pace.presentation.top.home.HomeScreen
import com.rk.pace.presentation.top.you.YouScreen

data class BottomNavItems(
    val route: Route,
    val icon: ImageVector
)

val bottomNavItemsList = listOf(
    BottomNavItems(
        route = Route.Top.Home,
        icon = Icons.Default.Home
    ),
    BottomNavItems(
        route = Route.Root.Run,
        icon = Icons.AutoMirrored.Filled.DirectionsRun
    ),
    BottomNavItems(
        route = Route.Top.You,
        icon = Icons.Default.Person
    )
)

@Composable
fun TopScreen(
    navController: NavController
) {
    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItemsList.forEach { item ->
                    NavigationBarItem(
                        onClick = {
                            if (item.route is Route.Root.Run) {
                                navController.navigate(item.route)
                            } else {
                                innerNavController.navigate(item.route) {
                                    popUpTo(Route.Top.Home) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
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
        }
    ) { it ->
        NavHost(
            navController = innerNavController,
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