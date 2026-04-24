package com.rk.pace.presentation.screens.active_run

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveRunScreenRoot(
    viewModel: ActiveRunViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val runState by viewModel.runState.collectAsStateWithLifecycle()

    SaveRunScreen(
        state = state,
        runState = runState,
        onAction = viewModel::onAction,
        onBack = onBack,
    )

}
