package com.rk.pace.data.room.entity

import androidx.room.Embedded
import androidx.room.Relation

data class RunWithPathEntity(
    @Embedded val run: RunEntity,
    @Relation(
        parentColumn = "runId",
        entityColumn = "runId"
    )
    val path: List<RunPathPointEntity>
)
