package com.rk.pace.di

import android.content.Context
import androidx.work.WorkManager
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.di.CoroutineDispatcherModule.provideIoDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager {
        return WorkManager.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideInternalStorageHelper(
        @ApplicationContext context: Context
    ) = InternalStorageHelper(context, provideIoDispatcher())

}