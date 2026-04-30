package com.rk.pace.data.tracker

import android.os.SystemClock
import com.rk.pace.di.ApplicationDefaultCoroutineScope
import com.rk.pace.di.DefaultDispatcher
import com.rk.pace.domain.tracker.TimeTracker
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

class TimeTrackerImp @Inject constructor(
    @param:DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @param:ApplicationDefaultCoroutineScope private val scope: CoroutineScope
) : TimeTracker {

    private var isAct = false
    private var callBack: ((time: Long) -> Unit)? = null
    private var job: Job? = null

    private var time = 0L
    private var segmentStartTime = 0L

    private fun start() {
        segmentStartTime = SystemClock.elapsedRealtime()
        if (job == null)
            this.job = scope.launch(defaultDispatcher) {
                while (isAct && this.isActive) {

                    val currentTime = SystemClock.elapsedRealtime()
                    val segmentTime = currentTime - segmentStartTime
                    val total = time + segmentTime

                    callBack?.invoke(total)
                    delay(1000)
                }
            }
    }

    override fun startTimer(callBack: (Long) -> Unit) {
        if (isAct) return
        this.callBack = callBack
        isAct = true
        time = 0L
        start()
    }

    override fun resumeTimer(callBack: (Long) -> Unit) {
        if (isAct) return
        this.callBack = callBack
        isAct = true
        start()
    }

    override fun pauseTimer() {
        isAct = false
        job?.cancel()
        job = null

        if (segmentStartTime > 0) {
            time += (SystemClock.elapsedRealtime() - segmentStartTime)
            segmentStartTime = 0L
        }

        callBack = null
    }

    override fun stopTimer() {
        pauseTimer()
        time = 0L
        segmentStartTime = 0L
    }

}