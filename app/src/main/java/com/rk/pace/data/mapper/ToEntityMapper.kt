package com.rk.pace.data.mapper

import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.data.remote.dto.RunPathPointDto
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.UserEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.User

fun Run.toEntity(): RunEntity {
    return RunEntity(
        runId = runId,
        userId = userId,
        timestamp = timestamp,
        durationMilliseconds = durationMilliseconds,
        distanceMeters = distanceMeters,
        avgSpeedMps = avgSpeedMps,
        encodedPath = encodedPath,
        title = title
    )
}

fun RunPathPoint.toEntity(runId: String): RunPathPointEntity {
    return RunPathPointEntity(
        runId = runId,
        timestamp = timestamp,
        lat = lat,
        long = long,
        speedMps = speedMps,
        isPausePoint = isPausePoint
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = userId,
        username = this.username,
        name = this.name,
        email = email,
        photoURI = photoURI,
        followers = followers,
        following = following
    )
}

fun RunDto.toEntity(): RunEntity {
    return RunEntity(
        runId = this.runId,
        userId = this.userId,
        timestamp = this.timestamp,
        durationMilliseconds = this.durationMilliseconds,
        distanceMeters = this.distanceMeters,
        avgSpeedMps = this.avgSpeedMps,
        encodedPath = encodedPath,
        synced = true,
        title = title
    )
}

fun RunPathPointDto.toEntity(runId: String): RunPathPointEntity {
    return RunPathPointEntity(
        runId = runId,
        timestamp = this.timestamp,
        lat = this.lat,
        long = this.long,
        speedMps = this.speedMps,
        isPausePoint = this.isPausePoint
    )
}