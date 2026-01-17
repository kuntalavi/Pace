package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import kotlinx.coroutines.flow.Flow

interface RunRepo {

    suspend fun insertRun(run: RunWithPath)

    suspend fun removeRun(run: Run)

    fun getARuns(): Flow<List<Run>>

    fun getARunsWithPath(): Flow<List<RunWithPath>>

    suspend fun getRunWithPathByRunId(runId: String): RunWithPath?

    suspend fun restoreRuns(userId: String)

}