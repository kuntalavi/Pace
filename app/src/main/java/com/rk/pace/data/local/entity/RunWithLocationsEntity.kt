package com.rk.pace.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation


data class RunWithLocationsEntity(
    @Embedded
    val run: RunEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "runId"
    )
    val locations: List<RunLocationEntity>
)