package com.rk.pace.common.ut

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale

object TimestampUt {

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

    fun getDayShort(timestamp: Long): String {
        val day = SimpleDateFormat("EEE", Locale.getDefault())
        return day.format(Date(timestamp))
    }

    fun getDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(date)
    }

    fun getBarDate(timestamp: Long): String {
        val date = Date(timestamp)
        val formatter = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
        return formatter.format(date)
    }

}