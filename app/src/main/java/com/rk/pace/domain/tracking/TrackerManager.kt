package com.rk.pace.domain.tracking

import com.rk.pace.domain.model.ActiveRun
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrackerManager @Inject constructor(
    private val backgroundTracker: BackgroundTracker,
    private val locationTracker: LocationTracker,
    private val timeTracker: TimeTracker
) {

    private val _activeRunState = MutableStateFlow(ActiveRun())
    val activeRunState = _activeRunState

    private val _durationInMillis = MutableStateFlow(0L)
    val durationInMillis = _durationInMillis.asStateFlow()


    fun startResumeTracking() {}

    fun pauseTracking() {}

    fun stopTracking() {}
}