package com.rk.pace.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.home.components.RunItem


@Composable
fun HomeScreen(
    viewmodel: HomeViewModel = hiltViewModel()
) {
    val runs by viewmodel.runs.collectAsStateWithLifecycle()

    LaunchedEffect(runs) {

    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(runs) { run ->
            RunItem(
                run = run
            )
        }
    }
}