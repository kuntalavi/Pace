package com.rk.pace.presentation.screens.run_stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.presentation.ut.ChartsUt.buildPaceChartData
import com.rk.pace.presentation.ut.PathUt.toSegments
import com.rk.pace.presentation.ut.ChartsUt.buildSplitChartData
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.PacePoint
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.Split
import com.rk.pace.domain.use_case.run.DeleteRunUseCase
import com.rk.pace.domain.use_case.run.GetRunWithPathByRunIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunStatsViewModel @Inject constructor(
    private val getRunWithPathByRunIdUseCase: GetRunWithPathByRunIdUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    getCurrentUserUseCase: GetCurrentUserIdUseCase,
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val userId: String = savedStateHandle["userId"] ?: ""
    val runId: String = savedStateHandle["runId"] ?: ""

    private val _isCurrentUser: MutableStateFlow<Boolean> =
        MutableStateFlow(
            getCurrentUserUseCase() == userId
        )
    val isCurrentUser = _isCurrentUser.asStateFlow()

    private val _state: MutableStateFlow<RunStatsState> = MutableStateFlow(RunStatsState.Load())
    val state = _state.asStateFlow()

    private val _splits: MutableStateFlow<List<Split>> = MutableStateFlow(emptyList())
    val splits = _splits.asStateFlow()

    private val _paceChartData: MutableStateFlow<List<PacePoint>> = MutableStateFlow(emptyList())
    val paceChartData = _paceChartData.asStateFlow()

    init {
        getRun(runId)
    }

    private fun getRun(runId: String) {
        if (runId.isEmpty()) return
        _state.update {
            RunStatsState.Load()
        }
        viewModelScope.launch {
            val runWithPath = getRunWithPathByRunIdUseCase(runId) ?: return@launch
            _state.update {
                RunStatsState.Success(runWithPath)
            }
            _splits.update {
                buildSplitChartData(runWithPath.path.toSegments())
            }
            _paceChartData.update {
                buildPaceChartData(runWithPath.path)
            }
        }
    }

    fun deleteRun(run: Run) {
        scope.launch { deleteRunUseCase(run) }
    }
}