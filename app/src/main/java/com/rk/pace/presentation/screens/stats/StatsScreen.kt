package com.rk.pace.presentation.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.components.RunSummary

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel(),
    goToRunStats: (userId: String, runId: String) -> Unit
) {
    val runs by viewModel.runs.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
    ) {
        item {
            Text(
                text = " RUNS",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    letterSpacing = 1.sp
                )
            )
        }
        items(runs) { run ->
            RunSummary(
                run,
                onClick = {
                    goToRunStats(
                        run.userId,
                        run.runId
                    )
                }
            )
        }
    }

}