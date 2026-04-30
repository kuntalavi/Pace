package com.rk.pace.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rk.pace.MainViewModel
import com.rk.pace.presentation.ut.defaultEnterTransition
import com.rk.pace.presentation.ut.defaultExitTransition
import com.rk.pace.presentation.ut.defaultPopEnterTransition
import com.rk.pace.presentation.ut.defaultPopExitTransition
import com.rk.pace.presentation.theme.add_people
import kotlin.reflect.KClass

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaceNavGraph(
    viewModel: MainViewModel = hiltViewModel(),
    startDestination: Route
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination

    val isRunAct by viewModel.isRunAct.collectAsStateWithLifecycle()
    val feed = destination?.hasRoute<Route.BotNav.Feed>() ?: false
    val run = destination?.hasRoute<Route.ActiveRun.Run>() ?: false
    val progress = destination?.hasRoute<Route.BotNav.Stats>() ?: false
    val profile = destination?.hasRoute<Route.BotNav.MyProfile>() ?: false

    val (showBotNavBar, showTopBar, title) = remember(destination, isRunAct) {
        when {
            destination == null -> Triple(false, false, "")
            feed -> Triple(true, true, "FEED")
            progress -> Triple(true, true, "STATS")
            profile -> Triple(true, true, "PROFILE")
            run -> Triple(!isRunAct, false, "")
            else -> Triple(false, false, "")
        }
    }

    val density = LocalDensity.current
    var bottomNavHeight by remember { mutableStateOf(100.dp) }

    val animatedBottomPadding by animateDpAsState(
        targetValue = if (showBotNavBar) bottomNavHeight else 0.dp,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Scaffold(
        topBar = {
            if (showTopBar) {
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
                        if (feed) {
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
            }
        }
    ) { p ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = if (showTopBar) p.calculateTopPadding() else 0.dp)
        ) {
            NavHost(
                modifier = Modifier.padding(
                    bottom = animatedBottomPadding
                ),
                navController = navController,
                startDestination = startDestination,
                enterTransition = { defaultEnterTransition() },
                exitTransition = { defaultExitTransition() },
                popEnterTransition = { defaultPopEnterTransition() },
                popExitTransition = { defaultPopExitTransition() }
            ) {

                rootNavGraph(navController)

                authNavGraph(navController)

                botNavGraph(navController)

                activeRunNavGraph(navController)

            }

            AnimatedVisibility(
                visible = showBotNavBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .onSizeChanged(
                        onSizeChanged = { size ->
                            bottomNavHeight = with(density) { size.height.toDp() }
                        }
                    )
            ) {
                PaceBotNavBar(
                    destination = destination
                ) { route ->
                    navController.navigate(route) {
                        popUpTo(Route.BotNav.Feed) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
    route: KClass<*>
): T {
    val navGraphRoute = route.qualifiedName ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}