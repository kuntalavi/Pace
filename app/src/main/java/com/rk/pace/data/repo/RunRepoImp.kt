package com.rk.pace.data.repo

import com.rk.pace.data.local.dao.RunDao
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunLocation
import com.rk.pace.domain.model.RunWithLocations
import com.rk.pace.domain.repo.RunRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RunRepoImp @Inject constructor(
    private val runDao: RunDao
) : RunRepo {
    override suspend fun insertRun(run: Run, locations: List<RunLocation>) {
        runDao.insertRun(run.toEntity())
        val locationsWithRunId = locations.map {
            it.copy(runId = run.id).toEntity()
        }
        runDao.insertLocations(locationsWithRunId)
    }

    override suspend fun deleteRun(run: Run) {
        runDao.deleteRun(run.toEntity())
    }

    override fun getAllRuns(): Flow<List<Run>> {
        return runDao.getAllRuns().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getRunWithLocations(runId: Long): RunWithLocations? {
        return runDao.getRunWithLocations(runId)?.toDomain()
    }
}