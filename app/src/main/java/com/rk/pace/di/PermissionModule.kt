package com.rk.pace.di

import com.rk.pace.data.permission.PermissionManagerImp
import com.rk.pace.data.permission.UserPrefImp
import com.rk.pace.domain.permission.PermissionManager
import com.rk.pace.domain.permission.UserPref
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionModule {

    @Binds
    @Singleton
    abstract fun bindPermissionManager(
        permissionManger: PermissionManagerImp
    ): PermissionManager

    @Binds
    @Singleton
    abstract fun bindUserPref(
        userPref: UserPrefImp
    ): UserPref

}