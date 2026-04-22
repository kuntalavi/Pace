package com.rk.pace.presentation.screens.stats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.presentation.components.PaceButton
import com.rk.pace.theme.arrowLeft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: StatsViewModel = hiltViewModel(),
    goBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ADD GOAL",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { }
                    ) {
                        Icon(
                            imageVector = arrowLeft,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { i ->
        Column(
            modifier = modifier
                .padding(i)
                .fillMaxSize()
        ) {
            Text(
                text = "Goal Type"
            )
            Text(
                text = "Target"
            )
            PaceButton(
                onClick = {
                    viewModel.updateWeekGoals(
                        WeekGoals(
                            runs = 10,
                            distanceMeters = 10f,
                            durationMilliseconds = 10
                        )
                    )
                    goBack()
                },
                text = "START GOAL"
            )
        }
    }
}