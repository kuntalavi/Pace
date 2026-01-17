package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "delete_runs")
data class DeleteRunEntity(
    @PrimaryKey(autoGenerate = false)
    val runId: String,
    val userId: String
)
