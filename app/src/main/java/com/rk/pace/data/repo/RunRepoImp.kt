package com.rk.pace.data.repo

import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.data.room.RunDao
import com.rk.pace.data.room.RunPathPointDao
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class RunRepoImp @Inject constructor(
    private val runDao: RunDao,
    private val runPathPointDao: RunPathPointDao

) : RunRepo {
    override suspend fun insertRun(run: Run): Long {

        val runId = runDao.insertRun(run.toEntity())

        val runPath = run.path.map {
            it.toEntity(runId)
        }
        runPathPointDao.insertRunPath(runPath)
        return runId
    }

    override suspend fun removeRun(run: Run) {
        runDao.removeRun(run.toEntity())
    }

    override suspend fun getARuns(): List<Run> {
        return runDao.getARunsWithPath().map { it.toDomain() }
    }

    override suspend fun getRunById(runId: Long): Run? {
        return runDao.getRunWithPathByRunId(runId)?.toDomain()
    }
}