package com.rk.pace.di

import com.rk.pace.data.repo.RunRepoImp
import com.rk.pace.domain.repo.RunRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {

    @Binds
    @Singleton
    abstract fun bindRunRepo(
        runRepoImp: RunRepoImp
    ): RunRepo

}