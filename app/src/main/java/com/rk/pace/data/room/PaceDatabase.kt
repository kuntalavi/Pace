package com.rk.pace.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rk.pace.data.room.dao.DeleteRunDao
import com.rk.pace.data.room.dao.RunDao
import com.rk.pace.data.room.dao.RunPathPointDao
import com.rk.pace.data.room.dao.UserDao
import com.rk.pace.data.room.entity.DeleteRunEntity
import com.rk.pace.data.room.entity.RunEntity
import com.rk.pace.data.room.entity.RunPathPointEntity
import com.rk.pace.data.room.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        RunEntity::class,
        RunPathPointEntity::class,
        DeleteRunEntity::class
    ],
    version = 12,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PaceDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun runDao(): RunDao
    abstract fun runPathPointDao(): RunPathPointDao
    abstract fun deleteRunDao(): DeleteRunDao
}