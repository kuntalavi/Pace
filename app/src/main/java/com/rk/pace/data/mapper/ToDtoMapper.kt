package com.rk.pace.data.mapper

import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.data.remote.dto.RunPathPointDto
import com.rk.pace.data.remote.dto.UserDto
import com.rk.pace.data.room.entity.RunWithPathEntity
import com.rk.pace.domain.model.User

fun RunWithPathEntity.toDto(): RunDto {
    return RunDto(
        runId = this.run.runId,
        userId = this.run.userId,
        timestamp = this.run.timestamp,
        durationMilliseconds = this.run.durationMilliseconds,
        distanceMeters = this.run.distanceMeters,
        avgSpeedMps = this.run.avgSpeedMps,
        encodedPath = this.run.encodedPath,
        title = this.run.title,
        pathPoints = this.path.map { point ->
            RunPathPointDto(
                timestamp = point.timestamp,
                lat = point.lat,
                long = point.long,
                speedMps = point.speedMps,
                isPausePoint = point.isPausePoint
            )
        }
    )
}

fun User.toDto(): UserDto {
    return UserDto(
        userId = this.userId,
        username = this.username,
        name = this.name,
        email = email,
        photoURL = photoURL,
        followers = followers,
        following = following
    )
}