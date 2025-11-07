package com.rk.pace.data.repo

import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.data.room.RunDao
import com.rk.pace.data.room.RunPathPointDao
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunRepoImp @Inject constructor(
    private val runDao: RunDao,
    private val runPathPointDao: RunPathPointDao

) : RunRepo {
    override suspend fun insertRun(run: RunWithPath): Long {

        val runId = runDao.insertRun(run.run.toEntity())

        val runPath = run.path.map {
            it.toEntity(runId)
        }
        runPathPointDao.insertRunPath(runPath)
        return runId
    }

    override suspend fun removeRun(run: Run) {
        runDao.removeRun(run.toEntity())
    }

    override fun getARuns(): Flow<List<Run>> {
        return runDao.getARuns().map { runE ->
            runE.map { it.toDomain() }
        }
    }

    override suspend fun getRunById(runId: Long): RunWithPath? {
        return runDao.getRunWithPathByRunId(runId)?.toDomain()
    }
}