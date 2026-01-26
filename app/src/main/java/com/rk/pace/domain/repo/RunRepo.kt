package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import kotlinx.coroutines.flow.Flow

interface RunRepo {

    suspend fun saveRun(run: RunWithPath)

    suspend fun removeRun(run: Run)

    suspend fun getUserRuns(userId: String): List<Run>

    fun getARuns(): Flow<List<Run>>

    fun getARunsWithPath(): Flow<List<RunWithPath>>

    suspend fun getRunWithPathByRunId(runId: String): RunWithPath?

    suspend fun restoreRuns(userId: String)

}