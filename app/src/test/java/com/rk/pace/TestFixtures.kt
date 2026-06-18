package com.rk.pace

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunPathPoint
import com.rk.pace.domain.model.RunWithPath

val fakeRun = Run(
    runId = "run-123",
    userId = "user-456",
    timestamp = 1_000_000L,
    durationMilliseconds = 1_800_000L,
    distanceMeters = 5000f,
    avgSpeedMps = 2.77f,
    encodedPath = emptyList(),
    title = "Run",
    likes = 0,
    likedBy = emptyList()
)

val fakePathPoints = listOf(
    RunPathPoint(
        timestamp = 0L,
        lat = 28.61,
        long = 77.20,
        speedMps = 2.5f
    ),
    RunPathPoint(
        timestamp = 1000L,
        lat = 28.62,
        long = 77.21,
        speedMps = 2.8f
    )
)

val fakeRunWithPath = RunWithPath(
    run = fakeRun,
    path = fakePathPoints
)