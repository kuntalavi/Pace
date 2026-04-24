package com.rk.pace.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PassiveLocationRequest

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActiveTrackLocationRequest