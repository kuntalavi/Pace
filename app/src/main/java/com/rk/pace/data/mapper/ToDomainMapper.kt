package com.rk.pace.data.mapper

import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.RunWithPathEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint

fun RunPathPointEntity.toDomain(): RunPathPoint {
    return RunPathPoint(
        timestamp = timestamp,
        lat = lat,
        l = l,
        speedMps = speedMps
    )
}

fun RunWithPathEntity.toDomain(): Run {
    return Run(
        runId = run.runId,
        startTime = run.startTime,
        endTime = run.endTime,
        distanceMeters = run.distanceMeters,
        durationM = run.durationM,
        avgPace = run.avgPace,
        maxPace = run.maxPace,
        path = path.map { it.toDomain() }
    )
}