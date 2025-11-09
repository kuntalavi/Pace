package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "runs"
)
data class RunEntity(
    @PrimaryKey(autoGenerate = true)
    val runId: Long,
    val timestamp: Long,
    val durationInM: Long,
    val distanceInMeters: Float,
    val avgSpeedMps: Float,
    val maxSpeedMps: Float,
    val bitmapURI: String?
)