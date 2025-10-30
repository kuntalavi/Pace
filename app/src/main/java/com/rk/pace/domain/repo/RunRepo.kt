package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunLocation
import com.rk.pace.domain.model.RunWithLocations
import kotlinx.coroutines.flow.Flow

interface RunRepo {
    suspend fun insertRun(run: Run, locations: List<RunLocation>)
    suspend fun deleteRun(run: Run)
    fun getAllRuns(): Flow<List<Run>>
    suspend fun getRunWithLocations(runId: Long): RunWithLocations?
}