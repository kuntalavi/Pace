package com.rk.pace.domain.ut

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

object WeekBoundsUt {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekBounds(weekOffset: Int): Pair<Long, Long> {
        val t = LocalDate.now()

        val monday = t
            .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            .plusWeeks(weekOffset.toLong())
        val sunday = monday.plusDays(6)

        val weekStart = monday.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val weekEnd =
            sunday.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        return weekStart to weekEnd
    }
}