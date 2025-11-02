package com.rk.pace.domain.tracking

interface TimeTracker {

    fun startResumeTimer(callback: (timeInMillis: Long) -> Unit)

    fun stopTimer()

    fun pauseTimer()
}