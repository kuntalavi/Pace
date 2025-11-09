package com.rk.pace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity

@Database(
    entities = [
        RunEntity::class,
//        RunPathEntity::class,
        RunPathPointEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PaceDatabase : RoomDatabase() {

    abstract fun runDao(): RunDao
    abstract fun runPathPointDao(): RunPathPointDao
}