package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "week_goals")
data class WeekGoalsEntity(
    @PrimaryKey
    val id: Int = 1,
    val runs: Int?,
    val distanceMeters: Float?,
    val durationMilliseconds: Long?
)
