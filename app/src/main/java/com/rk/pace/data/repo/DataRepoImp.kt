package com.rk.pace.data.repo

import android.content.Context
import androidx.work.WorkManager
import com.rk.pace.data.room.PaceDatabase
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.repo.DataRepo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepoImp @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val workManager: WorkManager,
    private val roomDB: PaceDatabase,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : DataRepo {
    override suspend fun deleteUserData() = withContext(ioDispatcher) {

        workManager.cancelAllWork()
        roomDB.clearAllTables()

        context.filesDir.listFiles()?.forEach { file ->
            file.deleteRecursively()
        }

        context.cacheDir.deleteRecursively()

        context.externalCacheDir?.deleteRecursively()

    }
}