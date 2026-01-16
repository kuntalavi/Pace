package com.rk.pace.data.mapper

import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.data.remote.dto.UserDto
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.RunWithPathEntity
import com.rk.pace.data.room.entity.UserEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.User

fun RunEntity.toDomain(): Run {
    return Run(
        runId = runId,
        userId = userId,
        timestamp = timestamp,
        durationMilliseconds = durationMilliseconds,
        distanceMeters = distanceMeters,
        avgSpeedMps = avgSpeedMps,
        encodedPath = encodedPath,
        title = title,
        likes = 0, //
        likedBy = emptyList() //
    )
}

fun RunPathPointEntity.toDomain(): RunPathPoint {
    return RunPathPoint(
        timestamp = timestamp,
        lat = lat,
        long = long,
        speedMps = speedMps,
        isPausePoint = isPausePoint
    )
}

fun RunWithPathEntity.toDomain(): RunWithPath {
    return RunWithPath(
        run = run.toDomain(),
        path = path.map { it.toDomain() }
    )
}

fun UserEntity.toDomain(): User {
    return User(
        userId = userId,
        username = this.username,
        name = this.name,
        email = email,
        photoURL = "",
        photoURI = photoURI,
        followers = followers,
        following = following
    )
}

fun UserDto.toDomain(photoURI: String?): User {
    return User(
        userId = userId,
        username = this.username,
        name = this.name,
        email = email,
        photoURL = photoURL,
        photoURI = photoURI,
        followers = followers,
        following = following
    )
}

fun RunDto.toDomain(): Run {
    return Run(
        runId = runId,
        userId = userId,
        timestamp = timestamp,
        durationMilliseconds = durationMilliseconds,
        distanceMeters = distanceMeters,
        avgSpeedMps = avgSpeedMps,
        encodedPath = encodedPath,
        title = title,
        likes = likes, //
        likedBy = likedBy //
    )
}