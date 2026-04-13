package com.rk.pace.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rk.pace.data.room.entity.WeekGoalsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeekGoalsDao {
    @Query("SELECT * FROM week_goals WHERE id = 1")
    fun getGoals(): Flow<WeekGoalsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWeekGoals(goals: WeekGoalsEntity)
}