package com.rk.pace.data.mapper

import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.RunWithPathEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.model.RunPathPoint

fun RunEntity.toDomain(): Run {
    return Run(
        runId = runId,
        timestamp = timestamp,
        distanceMeters = distanceInMeters,
        durationM = durationInM,
        avgSpeedMps = avgSpeedMps,
        maxSpeedMps = maxSpeedMps,
        bitmapURI = bitmapURI
    )
}

fun RunPathPointEntity.toDomain(): RunPathPoint {
    return RunPathPoint(
        timestamp = timestamp,
        lat = lat,
        l = l,
        speedMps = speedMps
    )
}

fun RunWithPathEntity.toDomain(): RunWithPath {
    return RunWithPath(
        run = run.toDomain(),
        path = path.map { it.toDomain() }
    )
}