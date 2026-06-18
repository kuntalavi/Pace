package com.rk.pace.data

import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.UserEntity

val fakeUser = UserEntity(
    userId = "user-001",
    username = "ravi_k",
    name = "Ravi Kumar",
    email = "ravi@example.com",
    photoURI = null,
    followers = 0,
    following = 0
)

val fakeRun = RunEntity(
    runId = "run-001",
    userId = "user-001",
    timestamp = 1_000_000L,
    durationMilliseconds = 1_800_000L,
    distanceMeters = 5000f,
    avgSpeedMps = 2.77f,
    encodedPath = emptyList(),
    synced = false,
    title = "Morning Run"
)

val fakeRun2 = RunEntity(
    runId = "run-002",
    userId = "user-001",
    timestamp = 2_000_000L,
    durationMilliseconds = 2_400_000L,
    distanceMeters = 8000f,
    avgSpeedMps = 3.1f,
    encodedPath = emptyList(),
    synced = true,
    title = "Evening Run"
)

val fakePathPoints = listOf(
    RunPathPointEntity(
        runId = "run-001",
        timestamp = 1_000_000L,
        lat = 28.61,
        long = 77.20,
        speedMps = 2.5f,
        pausePoint = false
    ),
    RunPathPointEntity(
        runId = "run-001",
        timestamp = 1_001_000L,
        lat = 28.62,
        long = 77.21,
        speedMps = 2.8f,
        pausePoint = false
    )
)