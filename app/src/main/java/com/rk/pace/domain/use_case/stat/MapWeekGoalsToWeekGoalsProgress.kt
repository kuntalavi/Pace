package com.rk.pace.domain.use_case.stat

import com.rk.pace.domain.model.GoalProgress
import com.rk.pace.domain.model.GoalType
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.model.WeekGoalsProgress
import javax.inject.Inject

class MapWeekGoalsToWeekGoalsProgress @Inject constructor() {
    operator fun invoke(weekRuns: List<Run>, weekGoals: WeekGoals,): WeekGoalsProgress {

        val distanceMeters = weekRuns.sumOf { run ->
            run.distanceMeters.toLong()
        }
        val durationMilliseconds = weekRuns.sumOf { run ->
            run.durationMilliseconds
        }

        val runGoalProgress = GoalProgress(
            type = GoalType.RUNS,
            progress = weekRuns.size.toFloat(),
            goal = weekGoals.runs?.toFloat() ?: 0f,
            isSet = weekGoals.runs != null
        )
        val distanceGoalProgress = GoalProgress(
            type = GoalType.DISTANCE_METERS,
            progress = distanceMeters.toFloat(),
            goal = weekGoals.distanceMeters ?: 0f,
            isSet = weekGoals.distanceMeters != null
        )
        val durationGoalProgress = GoalProgress(
            type = GoalType.DURATION_MILLISECONDS,
            progress = durationMilliseconds.toFloat(),
            goal = weekGoals.durationMilliseconds?.toFloat() ?: 0f,
            isSet = weekGoals.durationMilliseconds != null
        )

        return WeekGoalsProgress(
            runGoalProgress = runGoalProgress,
            distanceGoalProgress = distanceGoalProgress,
            durationGoalProgress = durationGoalProgress
        )
    }
}