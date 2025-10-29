package com.rk.pace.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "run_locations"
)
data class RunLocation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val runId: Long,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val accuracy: Float,
    val timestamp: Long
)
