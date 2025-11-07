package com.rk.pace.data.mapper

import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint

fun Run.toEntity(): RunEntity {
    return RunEntity(
        runId = runId,
        timestamp = timestamp,
        durationInM = durationM,
        distanceInMeters = distanceMeters,
        avgSpeedMps = avgSpeedMps,
        maxSpeedMps = maxSpeedMps,
        ePath = ePath
    )
}

fun RunPathPoint.toEntity(runId: Long): RunPathPointEntity {
    return RunPathPointEntity(
        runId = runId,
        timestamp = timestamp,
        lat = lat,
        l = l,
        speedMps = speedMps
    )
}