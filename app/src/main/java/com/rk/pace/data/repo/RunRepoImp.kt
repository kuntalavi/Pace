package com.rk.pace.data.repo

import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.firestore.FirebaseFirestore
import com.rk.pace.background.SyncRunWorker
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.data.mapper.toRunWithPathDomain
import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.data.remote.source.FirebaseRunDataSource
import com.rk.pace.data.room.dao.DeleteRunDao
import com.rk.pace.data.room.dao.RunDao
import com.rk.pace.data.room.dao.RunPathPointDao
import com.rk.pace.data.room.entity.DeleteRunEntity
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RunRepoImp @Inject constructor(
    private val runDao: RunDao,
    private val runPathPointDao: RunPathPointDao,
    private val deleteRunDao: DeleteRunDao,
    private val firestore: FirebaseFirestore,
    private val firebaseRunDataSource: FirebaseRunDataSource,
    private val workManager: WorkManager
) : RunRepo {
    override suspend fun saveRun(run: RunWithPath) {

        runDao.insertRun(run.run.toEntity())

        val runPath = run.path.map {
            it.toEntity(run.run.runId)
        }
        runPathPointDao.insertRunPath(runPath)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncRunWorker>()
            .setConstraints(constraints)
            .build()

        workManager.enqueueUniqueWork(
            "sync_runs",
            ExistingWorkPolicy.APPEND_OR_REPLACE,
            syncRequest
        )

    }

    override suspend fun removeRun(run: Run) {
        runDao.deleteRun(run.toEntity())

        deleteRunDao.insertDeleteRun(
            DeleteRunEntity(
                runId = run.runId,
                userId = run.userId
            )
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = OneTimeWorkRequestBuilder<SyncRunWorker>()
            .setConstraints(constraints)
            .build()


        workManager.enqueue(syncRequest)

    }

    override fun getARuns(): Flow<List<Run>> {
        return runDao.getAllRuns().map { it ->
            it.map { it.toDomain() }
        }
    }

    override suspend fun getUserRuns(userId: String): List<Run> {
        val runDtos = firebaseRunDataSource.getUserRuns(userId)
        val runs = runDtos.map { runDto ->
            runDto.toDomain()
        }
        return runs
    }

    override fun getARunsWithPath(): Flow<List<RunWithPath>> {
        return runDao.getAllRunsWithPath().map { it ->
            it.map { it.toDomain() }
        }
    }

    override suspend fun getRunWithPathByRunId(runId: String): RunWithPath? {
        val runEntity = runDao.getRunWithPathByRunId(runId)
        if (runEntity != null) {
            return runEntity.toDomain()
        }
        val runDto = firebaseRunDataSource.getRunDtoByRunId(runId)
        if (runDto != null) {
            return runDto.toRunWithPathDomain()
        }
        return null
    }

    override suspend fun restoreRuns(userId: String) {
        try {
            val snapshot = firestore.collection("runs")
                .whereEqualTo("userId", userId)
                .get()
                .await()

            val remoteRuns = snapshot.toObjects(RunDto::class.java)

            remoteRuns.forEach { runDto ->

                val runEntity = runDto.toEntity() // WORK
                val runPathPointEntities = runDto.pathPoints.map {
                    it.toEntity(runId = runDto.runId)
                }

                runDao.saveRestoredRun(runEntity, runPathPointEntities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}