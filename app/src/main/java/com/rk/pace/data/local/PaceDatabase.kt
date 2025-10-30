package com.rk.pace.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rk.pace.data.local.dao.RunDao
import com.rk.pace.data.local.entity.RunEntity
import com.rk.pace.data.local.entity.RunLocationEntity

@Database(
    entities = [
        RunEntity::class,
        RunLocationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PaceDatabase : RoomDatabase() {
    abstract fun runDao(): RunDao
}