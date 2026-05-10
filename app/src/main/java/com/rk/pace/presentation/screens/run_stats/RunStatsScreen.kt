package com.rk.pace.presentation.screens.run_stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.presentation.screens.run_stats.components.RunStatsMap
import com.rk.pace.presentation.screens.run_stats.components.SheetContent
import com.rk.pace.presentation.theme.arrowLeft
import com.rk.pace.presentation.theme.delete
import com.rk.pace.presentation.ut.ObserveAsEvents
import com.rk.pace.presentation.ut.PathUt.toSegments
import com.rk.pace.presentation.ut.TimestampUt.getBarDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunStatsScreen(
    onBack: () -> Unit,
    viewModel: RunStatsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RunStatsEvent.RunDeleted -> onBack()
            is RunStatsEvent.Error -> {
                snackbarState.showSnackbar(
                    message = event.message,
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )
    var mapLoaded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        if (state.load) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else if (state.error != null) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.error!!
                )
            }

        } else if (state.data != null) {

            val data = state.data!!
            BottomSheetScaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarState) { data ->
                        Snackbar(
                            containerColor = colorScheme.errorContainer,
                            contentColor = colorScheme.onErrorContainer,
                            snackbarData = data
                        )
                    }
                },
                scaffoldState = scaffoldState,
                sheetPeekHeight = 500.dp,
                sheetMaxWidth = Dp.Unspecified,
                sheetContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colorScheme.background)
                            .heightIn(min = 500.dp)
                    ) {
                        SheetContent(
                            run = data.run,
                            splits = data.splits,
                            paceChartData = data.paceChartData
                        )
                    }
                },
                sheetShape = RoundedCornerShape(0.dp),
                sheetDragHandle = null,
                sheetSwipeEnabled = true,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                text = getBarDate(data.run.timestamp),
                                letterSpacing = 1.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    onBack()
                                }
                            ) {
                                Icon(
                                    imageVector = arrowLeft,
                                    contentDescription = ""
                                )
                            }
                        },
                        actions = {
                            if (state.isCurrentUser) {
                                IconButton(
                                    onClick = {
                                        viewModel.onAction(
                                            RunStatsAction.OnDeleteRunClick
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = delete,
                                        contentDescription = ""
                                    )
                                }
                            }
                        }
                    )
                }
            ) {

                RunStatsMap(
                    modifier = Modifier
                        .fillMaxSize(),
                    segments = data.path.toSegments(),
                    bottomPaddingDp = 500.dp,
                    onMapLoadedCallback = {
                        mapLoaded = true
                    }
                )

                if (!mapLoaded) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

            }

        }

    }

}