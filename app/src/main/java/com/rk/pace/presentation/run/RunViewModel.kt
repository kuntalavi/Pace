package com.rk.pace.presentation.run

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunLocation
import com.rk.pace.domain.use_case.SaveRunUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val saveRunUseCase: SaveRunUseCase
) : ViewModel() {

    var hasLocationPermission by mutableStateOf(false)
        private set

    fun setLocationPermission(granted: Boolean) {
        hasLocationPermission = granted
    }

    var showRationale by mutableStateOf(false)
        private set

    fun showRationale() {
        showRationale = true
    }

    fun onRationaleDismissed() {
        showRationale = false
    }

    var isheaderExpanded by mutableStateOf(true)
        private set

    fun setHeaderExpanded(expanded: Boolean) {
        isheaderExpanded = expanded
    }

    private suspend fun saveRun(run: Run, locations: List<RunLocation>) {
        saveRunUseCase(run, locations)
    }
}