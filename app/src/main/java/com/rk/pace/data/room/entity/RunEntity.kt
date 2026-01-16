package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "runs",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["userId"])]
)
data class RunEntity(
    @PrimaryKey(autoGenerate = false)
    val runId: String,
    val userId: String,
    val timestamp: Long,
    val durationMilliseconds: Long,
    val distanceMeters: Float,
    val avgSpeedMps: Float,
    val encodedPath: List<String>,
    val synced: Boolean = false,
    val title: String = ""

    // likes and likedBy missing here reason is
)