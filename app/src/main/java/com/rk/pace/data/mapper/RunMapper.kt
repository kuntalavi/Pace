package com.rk.pace.data.mapper

import com.rk.pace.data.local.entity.RunEntity
import com.rk.pace.data.local.entity.RunLocationEntity
import com.rk.pace.data.local.entity.RunWithLocationsEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunLocation
import com.rk.pace.domain.model.RunWithLocations

fun RunEntity.toDomain(): Run {
    return Run(
        id = id,
        startTime = startTime,
        endTime = endTime,
        distanceMeters = distanceMeters,
        duration = duration,
        avgPace = avgPace,
        maxPace = maxPace,
        elevationGainMeters = elevationGainMeters
    )
}

fun Run.toEntity(): RunEntity {
    return RunEntity(
        id = id,
        startTime = startTime,
        endTime = endTime,
        distanceMeters = distanceMeters,
        duration = duration,
        avgPace = avgPace,
        maxPace = maxPace,
        elevationGainMeters = elevationGainMeters
    )
}

fun RunLocationEntity.toDomain(): RunLocation {
    return RunLocation(
        id = id,
        runId = runId,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        timestamp = timestamp,
        accuracy = accuracy,
        speedMps = speedMps
    )
}

fun RunLocation.toEntity(): RunLocationEntity {
    return RunLocationEntity(
        id = id,
        runId = runId,
        latitude = latitude,
        longitude = longitude,
        altitude = altitude,
        timestamp = timestamp,
        accuracy = accuracy,
        speedMps = speedMps
    )
}

fun RunWithLocationsEntity.toDomain(): RunWithLocations {
    return RunWithLocations(
        run = run.toDomain(),
        locations = locations.map { it.toDomain() }
    )
}