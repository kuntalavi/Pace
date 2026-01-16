package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "d_runs")
data class DeleteRunEntity(
    @PrimaryKey(autoGenerate = false)
    val runId: String,
    val userId: String
)
