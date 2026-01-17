package com.rk.pace.presentation.screens.active_run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.ButtonBox
import com.rk.pace.presentation.screens.run_stats.components.RunStatsMap
import com.rk.pace.presentation.screens.run_stats.components.Summary
import com.rk.pace.theme.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRunScreen(
    viewModel: ActiveRunViewModel,
    goBack: () -> Unit
) {

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val saved by viewModel.saved.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = saved) {
        if (saved) {
            viewModel.resetSaveState()
            goBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SAVE RUN",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            viewModel.stopRun()
                            viewModel.resetSaveState()
                            goBack()
                        }
                    ) {
                        Icon(
                            imageVector = delete,
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 15.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ButtonBox(
                    modifier = Modifier.fillMaxWidth(.9f),
                    onClick = {
                        viewModel.saveRun()
                    },
                    text = "SAVE"
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            RunStatsMap(
                segments = runState.segments,
                captureBitmap = state.captureBitmap,
                onBitmapReady = { bitmap ->
                    viewModel.onBitmapReady(bitmap)
                })
            Spacer(modifier = Modifier.height(16.dp))
            Summary(
                distance = runState.distanceMeters,
                time = runState.durationMilliseconds,
                speed = runState.avgSpeedMps
            )

        }
    }
}
