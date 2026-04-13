package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.WeekGoals
import kotlinx.coroutines.flow.Flow

interface StatRepo {
    fun getWeekRuns(weekStart: Long, weekEnd: Long): Flow<List<Run>>
    fun getWeekGoals(): Flow<WeekGoals>
    suspend fun updateWeekGoals(weekGoals: WeekGoals)
}