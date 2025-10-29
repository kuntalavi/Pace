package com.rk.pace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rk.pace.data.local.dao.RunDao
import com.rk.pace.data.local.entity.Run
import com.rk.pace.data.local.entity.RunLocation
import com.rk.pace.data.local.entity.RunWithLocation

@Database(
    entities = [
        Run::class,
        RunLocation::class,
        RunWithLocation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PaceDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}