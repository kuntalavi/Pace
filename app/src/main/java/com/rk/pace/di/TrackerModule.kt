package com.rk.pace.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rk.pace.background.service.RunTrackCImp
import com.rk.pace.data.tracking.LocationRequest
import com.rk.pace.data.tracking.LocationTrackerImp
import com.rk.pace.data.tracking.TimeTrackerImp
import com.rk.pace.data.tracking.TrackerManagerImp
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackC
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerModule {

    companion object {

        @Singleton
        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
        ) = LocationServices
            .getFusedLocationProviderClient(context)

        @Singleton
        @Provides
        fun provideLocationTracker(
            @ApplicationContext context: Context,
            @ApplicationDefaultCoroutineScope scope: CoroutineScope,
            fusedLocationProviderClient: FusedLocationProviderClient
        ): LocationTracker {
            return LocationTrackerImp(
                context = context,
                scope = scope,
                fusedLocationProviderClient = fusedLocationProviderClient,
                locationRequest = LocationRequest.locationRequest.build()
            )
        }

    }

    @Binds
    @Singleton
    abstract fun provideRunTrackC(
        backgroundTracker: RunTrackCImp
    ): RunTrackC

    @Binds
    @Singleton
    abstract fun provideTimeTracker(
        timeTracker: TimeTrackerImp
    ): TimeTracker

    @Binds
    @Singleton
    abstract fun provideTrackerManager(
        trackerManager: TrackerManagerImp
    ): TrackerManager

}