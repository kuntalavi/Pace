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

    @Singleton
    @Provides
    fun providePaceDatabase(
        @ApplicationContext context: Context
    ): PaceDatabase {
        return Room.databaseBuilder(
            context,
            PaceDatabase::class.java,
            PACE_DB_NAME,
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(database: PaceDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideRunDao(database: PaceDatabase) = database.runDao()

    @Singleton
    @Provides
    fun provideRunPathPointDao(database: PaceDatabase) = database.runPathPointDao()

    @Singleton
    @Provides
    fun provideDeleteRunDao(database: PaceDatabase) = database.deleteRunDao()
}