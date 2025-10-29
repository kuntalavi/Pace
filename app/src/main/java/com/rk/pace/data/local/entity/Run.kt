package com.rk.pace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "runs"
)
data class Run(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val userId: Long,
    val startTime: Long,
    val endTime: Long,
    val distanceMeters: Float,
    val duration: Long,
    val avgPace: Float,
    val maxPace: Float,
    val elevationGainMeters: Float
)
