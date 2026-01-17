package com.rk.pace.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rk.pace.data.room.entity.DeleteRunEntity

@Dao
interface DeleteRunDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDeleteRun(dRun: DeleteRunEntity)

    @Query("SELECT * FROM delete_runs")
    suspend fun getAllDeleteRuns(): List<DeleteRunEntity>

    @Delete
    suspend fun removeDeleteRun(dRun: DeleteRunEntity)

}