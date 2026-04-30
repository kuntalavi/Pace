package com.rk.pace

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.auth.domain.model.AuthState
import com.rk.pace.presentation.navigation.PaceNavGraph
import com.rk.pace.presentation.navigation.Route
import com.rk.pace.presentation.theme.PaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            mainViewModel.authState.value is AuthState.Load
        }

        enableEdgeToEdge()
        setContent {
            App()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun App(
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val authState by mainViewModel.authState.collectAsStateWithLifecycle()
    val startDestination = when (authState) {
        is AuthState.Authenticated -> Route.Root.BotNav
        is AuthState.Unauthenticated -> Route.Root.Auth
        else -> null
    }
    if (startDestination != null) {
        PaceTheme {
            PaceNavGraph(
                startDestination = startDestination
            )
        }
    }
}