package com.rk.pace.presentation.screens.stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.feed.components.FeedItemMap

@Composable
fun StatsScreen(
    viewModel: StatsViewModel = hiltViewModel()
) {
    val runs by viewModel.runs.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        contentPadding = PaddingValues(20.dp)
    ) {
        items(runs) { run ->
            FeedItemMap(
                path = run.encodedPath
            )
        }
    }

}