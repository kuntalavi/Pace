package com.rk.pace.data.local.dao

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface RunDao {

    @Insert
    suspend fun insertRun()
}