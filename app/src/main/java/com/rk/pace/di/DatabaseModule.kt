package com.rk.pace.di

import android.content.Context
import androidx.room.Room
import com.rk.pace.common.Constants.PACE_DB_NAME
import com.rk.pace.data.room.PaceDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePaceDatabase(
        @ApplicationContext context: Context
    ): PaceDatabase {
        return Room.databaseBuilder(
            context,
            PaceDatabase::class.java,
            PACE_DB_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun provideRunDao(database: PaceDatabase) = database.runDao()

    @Provides
    @Singleton
    fun provideRunPathPointDao(database: PaceDatabase) = database.runPathPointDao()
}