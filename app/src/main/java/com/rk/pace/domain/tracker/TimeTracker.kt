package com.rk.pace.domain.tracker

interface TimeTracker {

    fun startTimer(callBack: (time: Long) -> Unit)
    fun resumeTimer(callBack: (time: Long) -> Unit)
    fun stopTimer()
    fun pauseTimer()

}