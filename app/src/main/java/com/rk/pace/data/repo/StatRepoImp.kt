package com.rk.pace.data.repo

import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.room.dao.RunDao
import com.rk.pace.data.room.dao.WeekGoalsDao
import com.rk.pace.data.room.entity.WeekGoalsEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.repo.StatRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatRepoImp @Inject constructor(
    private val runDao: RunDao,
    private val weekGoalsDao: WeekGoalsDao
) : StatRepo {
    override fun getWeekRuns(
        weekStart: Long,
        weekEnd: Long
    ): Flow<List<Run>> {
        return runDao.getRunsForWeek(weekStart, weekEnd).map { runEntities ->
            runEntities.map { runEntity ->
                runEntity.toDomain()
            }
        }
    }

    override fun getWeekGoals(): Flow<WeekGoals> {
        return weekGoalsDao.getGoals().map { weekGoalsEntity ->
            WeekGoals(
                runs = weekGoalsEntity?.runs,
                distanceMeters = weekGoalsEntity?.distanceMeters,
                durationMilliseconds = weekGoalsEntity?.durationMilliseconds
            )
        }
    }

    override suspend fun updateWeekGoals(weekGoals: WeekGoals) {

        val weekGoalsEntity = WeekGoalsEntity(
            runs = weekGoals.runs,
            distanceMeters = weekGoals.distanceMeters,
            durationMilliseconds = weekGoals.durationMilliseconds
        )

        weekGoalsDao.updateWeekGoals(weekGoalsEntity)
    }
}