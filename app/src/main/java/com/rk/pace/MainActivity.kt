package com.rk.pace

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rk.pace.auth.presentation.AuthViewModel
import com.rk.pace.presentation.navigation.PaceNavGraph
import com.rk.pace.presentation.theme.PaceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().setKeepOnScreenCondition {
            authViewModel.startDestination.value == null
        }

        enableEdgeToEdge()
        setContent {
            val startDestination by authViewModel.startDestination.collectAsState()
            if (startDestination != null) {
                PaceTheme {
                    PaceNavGraph(
                        startDestination = startDestination!!
                    )
                }
            }
        }
    }
}
