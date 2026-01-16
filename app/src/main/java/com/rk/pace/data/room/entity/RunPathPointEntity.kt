package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "r_path_points",
    foreignKeys = [
        ForeignKey(
            entity = RunEntity::class,
            parentColumns = ["runId"],
            childColumns = ["runId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["runId"])]
)
data class RunPathPointEntity(
    @PrimaryKey(autoGenerate = true)
    val pointId: Long = 0,
    val runId: String,
    val timestamp: Long,
    val lat: Double,
    val long: Double,
    val speedMps: Float,
    val isPausePoint: Boolean
)
