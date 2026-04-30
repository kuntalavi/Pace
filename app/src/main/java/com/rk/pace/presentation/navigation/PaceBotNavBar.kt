package com.rk.pace.presentation.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.rk.pace.presentation.theme.feed
import com.rk.pace.presentation.theme.progress
import com.rk.pace.presentation.theme.run
import com.rk.pace.presentation.theme.user

data class BotNav(
    val route: Route,
    val icon: ImageVector,
    val iconSelected: ImageVector
)

@Composable
fun PaceBotNavBar(
    destination: NavDestination?,
    onTabClick: (Route) -> Unit
) {

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

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = colorScheme.surface
    ) {

        botNavList.forEach { i ->
            val selected = destination?.hasRoute(i.route::class) ?: (
                    i.route == Route.ActiveRun.Run
                    )

            val color by animateColorAsState(
                targetValue = if (selected) colorScheme.onPrimaryContainer
                else colorScheme.onSurfaceVariant,
                animationSpec = tween(200),
                label = ""
            )

            NavigationBarItem(
                selected = selected,
                onClick = {
                    onTabClick(
                        i.route
                    )
                },
                icon = {
                    Icon(
                        imageVector = if (selected) i.iconSelected else i.icon,
                        contentDescription = null,
                        tint = color
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = colorScheme.primaryContainer
                )
            )
        }

    }
}