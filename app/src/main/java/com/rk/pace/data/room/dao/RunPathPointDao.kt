package com.rk.pace.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.rk.pace.data.room.entity.RunPathPointEntity

@Dao
interface RunPathPointDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRunPath(runPath: List<RunPathPointEntity>)

}