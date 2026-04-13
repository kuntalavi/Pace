package com.rk.pace.domain.use_case.stat

import android.os.Build
import androidx.annotation.RequiresApi
import com.rk.pace.domain.model.DayDistance
import com.rk.pace.domain.model.Run
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class MapWeekRunsToChartData @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(runs: List<Run>, weekOffset: Int): List<DayDistance> {

        val t = LocalDate.now()
        val monday = t
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .plusWeeks(weekOffset.toLong())

        val distanceByDayIndex = Array(7) { 0f }

        runs.forEach { run ->
            val runDate = Instant.ofEpochMilli(run.timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            val dayIndex = ChronoUnit.DAYS.between(monday, runDate).toInt()
            if (dayIndex in 0..6) {
                distanceByDayIndex[dayIndex] += (run.distanceMeters)
            }
        }

        val labels = listOf("M", "T", "W", "T", "F", "S", "S")

        return labels.mapIndexed { index, label ->
            val date = monday.plusDays(index.toLong())
            DayDistance(
                day = label,
                distanceMeters = distanceByDayIndex[index],
                isToday = weekOffset == 0 && date == t
            )
        }

    }
}