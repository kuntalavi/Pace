package com.rk.pace.presentation.screens.run_stats

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.di.ApplicationIoCoroutineScope
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.use_case.run.DeleteRunUseCase
import com.rk.pace.domain.use_case.run.GetRunWithPathByUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunStatsViewModel @Inject constructor(
    private val getRunByUseCase: GetRunWithPathByUseCase,
    private val deleteRunUseCase: DeleteRunUseCase,
    @param:ApplicationIoCoroutineScope private val scope: CoroutineScope,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _run: MutableStateFlow<RunWithPath?> = MutableStateFlow(null)
    val run = _run

    init {
        val runId: String =
            checkNotNull(savedStateHandle["runId"])
        getRun(runId)
    }

    private fun getRun(runId: String) {
        viewModelScope.launch {
            val run = getRunByUseCase(runId) ?: return@launch
            _run.update {
                run
            }
        }
    }

    fun deleteRun(run: Run) {
        scope.launch { deleteRunUseCase(run) }
    }
}