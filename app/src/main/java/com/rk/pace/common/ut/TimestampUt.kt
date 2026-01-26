package com.rk.pace.common.ut

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimestampUt {

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