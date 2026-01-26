package com.rk.pace.presentation.screens.active_run

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.domain.model.Run
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.presentation.components.PaceInputBox
import com.rk.pace.presentation.screens.run_stats.components.RunStatsMap
import com.rk.pace.presentation.components.Summary
import com.rk.pace.theme.delete

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRunScreen(
    viewModel: ActiveRunViewModel,
    goBack: () -> Unit
) {

    val runState by viewModel.runState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var isMapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.isSaved) {
        if (state.isSaved) {
            goBack()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "SAVE RUN",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                viewModel.stopRun()
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
                        .padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PaceButton(
                        modifier = Modifier.fillMaxWidth(.9f),
                        text = "SAVE RUN",
                        onClick = {
                            viewModel.saveRun()
                        },
                        enabled = isMapLoaded && !state.isSav,
                        filled = true,
                        load = state.isSav
                    )
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                RunStatsMap(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    segments = runState.segments,
                    bottomPaddingDp = 0.dp,
                    onMapLoadedCallback = {
                        isMapLoaded = true
                    }
                )

                PaceInputBox(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    value = state.title,
                    onValueChange = { title ->
                        viewModel.onTitleChange(title)
                    },
                    placeholder = "TITLE"
                )

                Summary(
                    run = Run(
                        userId = "",
                        timestamp = runState.timestamp,
                        distanceMeters = runState.distanceMeters,
                        durationMilliseconds = runState.durationMilliseconds,
                        avgSpeedMps = runState.avgSpeedMps,
                        encodedPath = emptyList(),
                        title = state.title,
                        likes = 0,
                        likedBy = emptyList()
                    )
                )
            }
        }
        if (!isMapLoaded) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
