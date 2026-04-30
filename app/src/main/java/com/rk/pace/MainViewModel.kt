package com.rk.pace

import androidx.lifecycle.ViewModel
import com.rk.pace.domain.tracker.TrackerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    trackerManager: TrackerManager
): ViewModel() {

    val isRunAct = trackerManager.isAct
}