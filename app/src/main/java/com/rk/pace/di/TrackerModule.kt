package com.rk.pace.di

import android.content.Context
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.rk.pace.background.service.RunTrackServiceControllerImp
import com.rk.pace.data.tracking.GpsStatusTrackerImp
import com.rk.pace.data.tracking.LocationTrackerImp
import com.rk.pace.data.tracking.TimeTrackerImp
import com.rk.pace.data.tracking.TrackerManagerImp
import com.rk.pace.domain.tracking.GpsStatusTracker
import com.rk.pace.domain.tracking.LocationTracker
import com.rk.pace.domain.tracking.RunTrackServiceController
import com.rk.pace.domain.tracking.TimeTracker
import com.rk.pace.domain.tracking.TrackerManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TrackerModule {

    companion object {

        @Provides
        @Singleton
        @PassiveLocationRequest
        fun providePassiveLocationRequest(): LocationRequest {
            return LocationRequest.Builder(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                10000L
            )
                .setMinUpdateIntervalMillis(5000L)
                .build()
        }

        @Provides
        @Singleton
        @ActiveTrackLocationRequest
        fun provideActiveTrackLocationRequest(): LocationRequest {
            return LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                2000L
            )
                .setMinUpdateIntervalMillis(1000L)
                .setMinUpdateDistanceMeters(1f)
                .setWaitForAccurateLocation(true)
                .build()
        }

        @Singleton
        @Provides
        fun provideFusedLocationProviderClient(
            @ApplicationContext context: Context
        ) = LocationServices
            .getFusedLocationProviderClient(context)

    }

    @Binds
    @Singleton
    abstract fun provideLocationTracker(
        locationTracker: LocationTrackerImp
    ): LocationTracker

    @Binds
    @Singleton
    abstract fun provideGpsStatusTracker(
        gpsStatusTracker: GpsStatusTrackerImp
    ): GpsStatusTracker

    @Binds
    @Singleton
    abstract fun provideRunTrackServiceController(
        runTrackServiceController: RunTrackServiceControllerImp
    ): RunTrackServiceController

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