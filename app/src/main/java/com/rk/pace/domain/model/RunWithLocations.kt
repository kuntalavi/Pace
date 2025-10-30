package com.rk.pace.domain.model

data class RunWithLocations(
    val run: Run,
    val locations: List<RunLocation>
)
