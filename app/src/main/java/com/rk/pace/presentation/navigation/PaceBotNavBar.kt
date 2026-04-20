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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

@Composable
fun PaceBotNavBar(
    botNavList: List<BotNav>,
    destination: NavDestination?,
    onTabClick: (Route) -> Unit
) {

    if (destination == null) return

    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = colorScheme.surface
    ) {

        botNavList.forEach { i ->
            val selected = destination.hasRoute(i.route::class)

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