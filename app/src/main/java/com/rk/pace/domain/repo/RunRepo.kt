package com.rk.pace.domain.repo

import com.rk.pace.domain.model.Run

interface RunRepo {

    suspend fun insertRun(run: Run): Long

    suspend fun removeRun(run: Run)

    suspend fun getARuns(): List<Run>

    suspend fun getRunById(runId: Long): Run?
}