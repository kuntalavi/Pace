package com.rk.pace.presentation.screens.run_stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.common.ut.PathUt.toSegments
import com.rk.pace.presentation.screens.run_stats.components.RunStatsMap
import com.rk.pace.presentation.screens.run_stats.components.Summary
import com.rk.pace.theme.back
import com.rk.pace.theme.delete
import com.rk.pace.theme.share

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunStatsScreen(
    runId: String,
    goBack: () -> Unit,
    viewModel: RunStatsViewModel = hiltViewModel()
) {

    val run by viewModel.run.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = { goBack() }
                    ) {
                        Icon(
                            imageVector = back,
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {} // share
                    ) {
                        Icon(
                            imageVector = share,
                            contentDescription = ""
                        )
                    }
                    IconButton(
                        onClick = {
                            viewModel.deleteRun(run?.run ?: return@IconButton)
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
                segments = run?.path?.toSegments() ?: emptyList(),
                captureBitmap = false,
                onBitmapReady = {})
            Spacer(modifier = Modifier.height(16.dp))
            Summary(
                distance = run?.run?.distanceMeters ?: 0f,
                time = run?.run?.durationMilliseconds ?: 0L,
                speed = run?.run?.avgSpeedMps ?: 0f
            )
        }
    }
}