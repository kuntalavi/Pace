package com.rk.pace.data.tracking

import com.rk.pace.di.ApplicationScope
import com.rk.pace.di.DefaultDispatcher
import com.rk.pace.domain.tracking.TimeTracker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTrackerImp @Inject constructor(
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @param:ApplicationScope private val scope: CoroutineScope
) : TimeTracker {

    private var time = 0L
    private var isAct = false
    private var callBack: ((time: Long) -> Unit)? = null
    private var job: Job? = null

    private fun start() {
        if (job == null)
            this.job = scope.launch(defaultDispatcher) {
                while (isAct && this.isActive) {
                    delay(1000)
                    time += 1000
                    callBack?.invoke(time)
                }
            }
    }

    override fun startTimer(callBack: (Long) -> Unit) {
        if (time == 0L) {
            this.callBack = callBack
            isAct = true
            start()
        }
    }

    override fun resumeTimer(callBack: (Long) -> Unit) {
        if (!isAct) {
            this.callBack = callBack
            isAct = true
            start()
        }
    }

    override fun stopTimer() {
        pauseTimer()
        time = 0
    }

    override fun pauseTimer() {
        isAct = false
        job?.cancel()
        job = null
        callBack = null
    }
}