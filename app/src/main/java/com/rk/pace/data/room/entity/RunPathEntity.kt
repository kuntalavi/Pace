package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "r_paths",
    foreignKeys = [
        ForeignKey(
            entity = RunEntity::class,
            parentColumns = arrayOf("runId"),
            childColumns = arrayOf("runId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class RunPathEntity(
    @PrimaryKey
    val runId: Long,
    val path: String
)
