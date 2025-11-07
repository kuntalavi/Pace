package com.rk.pace.domain.tracking

interface TimeTracker {

    fun startTimer(cBack: (time: Long) -> Unit)
    fun resumeTimer(cBack: (time: Long) -> Unit)
    fun stopTimer()
    fun pauseTimer()
}