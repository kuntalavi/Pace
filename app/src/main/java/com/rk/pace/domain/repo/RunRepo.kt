package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import kotlinx.coroutines.flow.Flow

interface RunRepo {

    suspend fun insertRun(run: RunWithPath): Long

    suspend fun removeRun(run: Run)

    fun getARuns(): Flow<List<Run>>

    suspend fun getRunById(runId: Long): RunWithPath?
}