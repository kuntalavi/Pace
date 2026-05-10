package com.rk.pace.presentation.screens.run_stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.domain.use_case.run.DeleteRunUseCase
import com.rk.pace.domain.use_case.run.GetRunWithPathByRunIdUseCase
import com.rk.pace.presentation.ut.ChartsUt.buildPaceChartData
import com.rk.pace.presentation.ut.ChartsUt.buildSplitChartData
import com.rk.pace.presentation.ut.PathUt.toSegments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunStatsViewModel @Inject constructor(
    private val getRunWithPathByRunIdUseCase: GetRunWithPathByRunIdUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: String = savedStateHandle["userId"] ?: ""
    val runId: String = savedStateHandle["runId"] ?: ""

    private val _state: MutableStateFlow<RunStatsUiState> = MutableStateFlow(
        RunStatsUiState(
            isCurrentUser = getCurrentUserIdUseCase() == userId
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<RunStatsEvent>()
    val events = _events.receiveAsFlow()

    init {
        getData(runId)
    }

    private fun getData(runId: String) {

        if (runId.isEmpty()) {
            _state.update {
                it.copy(
                    load = false,
                    error = ""
                )
            }
            return
        }

        viewModelScope.launch {

            _state.update {
                it.copy(
                    load = false,
                    error = null
                )
            }

            val runWithPath = getRunWithPathByRunIdUseCase(runId)

            if (runWithPath == null) {
                _state.update {
                    it.copy(
                        load = false,
                        error = ""
                    )
                }
                return@launch
            }

            val splits = buildSplitChartData(
                runWithPath
                    .path
                    .toSegments()
            )
            val paceChartData = buildPaceChartData(
                runWithPath.path
            )


            _state.update {
                it.copy(
                    load = false,
                    data = RunStatsData(
                        run = runWithPath.run,
                        path = runWithPath.path,
                        splits = splits,
                        paceChartData = paceChartData
                    ),
                    error = null
                )
            }

        }

    }

    fun onAction(action: RunStatsAction) {
        when (action) {
            RunStatsAction.OnDeleteRunClick -> deleteRun()
        }
    }

    private fun deleteRun() {
        val run = _state.value.data?.run ?: return

        viewModelScope.launch {

            try {
                deleteRunUseCase(run)
                _events.send(
                    RunStatsEvent.RunDeleted
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _events.send(
                    RunStatsEvent.Error(message = "")
                )
            }

        }

    }

}