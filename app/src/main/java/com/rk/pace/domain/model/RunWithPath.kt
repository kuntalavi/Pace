package com.rk.pace.domain.model

data class RunWithPath(
    val run: Run,
    val path: List<RunPathPoint>
)
