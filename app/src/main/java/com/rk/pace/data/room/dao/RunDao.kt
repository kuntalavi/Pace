package com.rk.pace.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.RunWithPathEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    // RUN ENTITY

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity)

    @Delete
    suspend fun deleteRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRuns(): Flow<List<RunEntity>>

    // RUN PATH ENTITY

    // RELATIONS

    @Transaction
    @Query("SELECT * FROM runs WHERE runId = :runId")
    suspend fun getRunWithPathByRunId(runId: String): RunWithPathEntity?

    @Transaction
    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getAllRunsWithPath(): Flow<List<RunWithPathEntity>>

    // FIRESTORE SYNC

    @Transaction
    @Query("SELECT * FROM runs WHERE synced = 0")
    suspend fun getUnsyncedRunsWithPath(): List<RunWithPathEntity>

    @Query("UPDATE runs SET synced = 1 WHERE runId = :runId")
    suspend fun markRunAsSynced(runId: String)

    // FIRESTORE RESTORE

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunPathPoints(points: List<RunPathPointEntity>)

    @Transaction
    suspend fun saveRestoredRun(run: RunEntity, points: List<RunPathPointEntity>) {
        insertRun(run)
        insertRunPathPoints(points)
    }
}