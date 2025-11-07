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
    @param:ApplicationScope private val app: CoroutineScope,
    @param:DefaultDispatcher private val default: CoroutineDispatcher
) : TimeTracker {

    private var time = 0L
    private var isAct = false
    private var cBack: ((time: Long) -> Unit)? = null
    private var job: Job? = null

    private fun start() {
        if (job == null)
            this.job = app.launch(default) {
                while (isAct && this.isActive) {
                    delay(1000)
                    time += 1000
                    cBack ?. invoke(time)
                }
            }
    }

    override fun startTimer(cBack: (Long) -> Unit) {
        if (time == 0L){
            this.cBack = cBack
            isAct = true
            start()
        }
    }

    override fun resumeTimer(cBack: (Long) -> Unit) {
        if (!isAct){
            this.cBack = cBack
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
        cBack = null
    }
}