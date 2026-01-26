package com.rk.pace.presentation.screens.run_stats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rk.pace.common.ut.PathUt.toSegments
import com.rk.pace.common.ut.TimestampUt.getBarDate
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.presentation.screens.run_stats.components.RunStatsMap
import com.rk.pace.presentation.components.Summary
import com.rk.pace.theme.back
import com.rk.pace.theme.delete


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RunStatsScreen(
    goBack: () -> Unit,
    viewModel: RunStatsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val isCurrentUser by viewModel.isCurrentUser.collectAsStateWithLifecycle()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )
    var isMapLoaded by rememberSaveable { mutableStateOf(false) }

    when (val state = state) {

        is RunStatsState.Load -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is RunStatsState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = state.message
                )
            }
        }

        is RunStatsState.Success -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                BottomSheetScaffold(
                    scaffoldState = scaffoldState,
                    sheetPeekHeight = 500.dp,
                    sheetMaxWidth = Dp.Unspecified,
                    sheetContent = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .heightIn(min = 500.dp)
                        ) {
                            SheetContent(
                                run = state.runWithPath
                            )
                        }
                    },
                    sheetShape = RoundedCornerShape(0.dp),
                    sheetDragHandle = null,
                    sheetSwipeEnabled = false,
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = {
                                Text(
                                    text = getBarDate(state.runWithPath.run.timestamp),
                                    letterSpacing = 1.sp
                                )
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        goBack()
                                    }
                                ) {
                                    Icon(
                                        imageVector = back,
                                        contentDescription = ""
                                    )
                                }
                            },
                            actions = {
                                if (isCurrentUser) {
                                    IconButton(
                                        onClick = {
                                            viewModel.deleteRun(
                                                state.runWithPath.run
                                            )
                                            goBack()
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
                        segments = state.runWithPath.path.toSegments(),
                        bottomPaddingDp = 500.dp,
                        onMapLoadedCallback = {
                            isMapLoaded = true
                        }
                    )
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
        }
    }
}

@Composable
fun SheetContent(
    run: RunWithPath
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = run.run.title.uppercase().ifEmpty { "RUNNING" },
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp
        )

        Summary(
            run = run.run
        )
    }
}