package com.rk.pace.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rk.pace.data.local.entity.RunEntity
import com.rk.pace.data.local.entity.RunLocationEntity
import com.rk.pace.data.local.entity.RunWithLocationsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: RunEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocations(locations: List<RunLocationEntity>)

    @Delete
    suspend fun deleteRun(run: RunEntity)

    @Query("SELECT * FROM runs")
    fun getAllRuns(): Flow<List<RunEntity>>

    @Transaction
    @Query("SELECT * FROM runs WHERE id = :runId")
    suspend fun getRunWithLocations(runId: Long): RunWithLocationsEntity?

}