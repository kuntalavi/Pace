package com.rk.pace.presentation.screens.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.use_case.run.GetARunsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getRunsUseCase: GetARunsUseCase
) : ViewModel() {
    private val _runs = MutableStateFlow<List<Run>>(emptyList())
    val runs = _runs

    init {
        getRuns()
    }

    private fun getRuns() {
        viewModelScope.launch {
            _runs.update {
                emptyList()
            }
            getRunsUseCase().collect { runs ->
                _runs.update {
                    runs
                }
            }
        }
    }
}