package com.rk.pace.domain.tracking

interface TimeTracker {

    fun startTimer(callBack: (time: Long) -> Unit)
    fun resumeTimer(callBack: (time: Long) -> Unit)
    fun stopTimer()
    fun pauseTimer()
}