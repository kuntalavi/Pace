package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "runs"
)
data class RunEntity(
    @PrimaryKey(autoGenerate = true)
    val runId: Long,
    val startTime: Long,
    val endTime: Long,
    val distanceMeters: Float,
    val durationM: Long,
    val avgPace: Float,
    val maxPace: Float
)