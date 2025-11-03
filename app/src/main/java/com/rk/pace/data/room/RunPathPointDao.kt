package com.rk.pace.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.rk.pace.data.room.entity.RunPathPointEntity

@Dao
interface RunPathPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunPath(runPath: List<RunPathPointEntity>)

//    @Query("SELECT * FROM r_path_points WHERE runId = :runId ORDER BY timestamp ASC")
//    suspend fun getRunPathByRunId(runId: Long): List<RunPathPointEntity>

}