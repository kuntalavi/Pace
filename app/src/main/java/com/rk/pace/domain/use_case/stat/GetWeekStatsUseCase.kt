package com.rk.pace.domain.use_case.stat

import android.os.Build
import androidx.annotation.RequiresApi
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.WeekStats
import javax.inject.Inject

class GetWeekStatsUseCase @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(runs: List<Run>): WeekStats {
        if (runs.isEmpty()) {
            return WeekStats(
                runs = 0,
                distanceMeters = 0f,
                durationMilliseconds = 0L,
                avgSpeedMps = 0f
            )
        }

        val distanceMeters = runs.sumOf { run ->
            run.distanceMeters.toLong()
        }

        val durationMilliseconds = runs.sumOf { run ->
            run.durationMilliseconds
        }

        val avgSpeedMps = if (durationMilliseconds > 0) {
            distanceMeters / (durationMilliseconds / 1000f)
        } else 0f

        return WeekStats(
            runs = runs.size,
            distanceMeters = distanceMeters.toFloat(),
            durationMilliseconds = durationMilliseconds,
            avgSpeedMps = avgSpeedMps
        )
    }
}