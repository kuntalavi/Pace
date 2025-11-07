package com.rk.pace.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunWithPathEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    // RUN ENTITY

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity): Long

    @Delete
    suspend fun removeRun(run: RunEntity)

    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getARuns(): Flow<List<RunEntity>>

    // RUN PATH ENTITY

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertStringRunPath(stringRunPath: RunPathEntity)
//
//    @Query("SELECT * FROM r_paths WHERE runId = :runId")
//    suspend fun getStringRunPathById(runId: Long): RunPathEntity?

    // RELATIONS

    @Transaction
    @Query("SELECT * FROM runs WHERE runId = :runId")
    suspend fun getRunWithPathByRunId(runId: Long): RunWithPathEntity?

    @Transaction
    @Query("SELECT * FROM runs ORDER BY timestamp DESC")
    fun getARunsWithPath(): Flow<List<RunWithPathEntity>>
}

