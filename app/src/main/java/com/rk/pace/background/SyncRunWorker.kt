package com.rk.pace.background

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.rk.pace.data.mapper.toDto
import com.rk.pace.data.room.dao.DeleteRunDao
import com.rk.pace.data.room.dao.RunDao
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@HiltWorker
class SyncRunWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val runDao: RunDao,
    private val deleteRunDao: DeleteRunDao,
    private val firestore: FirebaseFirestore
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {

            val runsToDelete = deleteRunDao.getAllDeleteRuns() //

            runsToDelete.forEach { dRun ->
                try {
                    firestore.collection("runs")
                        .document(dRun.runId)
                        .delete()
                        .await()

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                deleteRunDao.removeDeleteRun(dRun)
            }

            val unsyncedRuns = runDao.getUnsyncedRunsWithPath() //
            if (unsyncedRuns.isEmpty()) {
                return@withContext Result.success()
            }

            unsyncedRuns.forEach { runWithPathEntity ->

                val runDto = runWithPathEntity.toDto()

                firestore.collection("runs")
                    .document(runDto.runId)
                    .set(runDto)
                    .await()

                runDao.markRunAsSynced(runDto.runId)

            }

            Result.success()

        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }

    }
}