package com.rk.pace.di

import com.rk.pace.auth.data.AuthRepoImp
import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.data.repo.DataRepoImp
import com.rk.pace.data.repo.FeedRepoImp
import com.rk.pace.data.repo.RunRepoImp
import com.rk.pace.data.repo.SocialRepoImp
import com.rk.pace.data.repo.UserRepoImp
import com.rk.pace.domain.repo.DataRepo
import com.rk.pace.domain.repo.FeedRepo
import com.rk.pace.domain.repo.RunRepo
import com.rk.pace.domain.repo.SocialRepo
import com.rk.pace.domain.repo.UserRepo
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
    abstract fun bindAuthRepo(
        authRepoImp: AuthRepoImp
    ): AuthRepo

    @Binds
    @Singleton
    abstract fun bindRunRepo(
        runRepoImp: RunRepoImp
    ): RunRepo

    @Binds
    @Singleton
    abstract fun bindUserRepo(
        userRepoImp: UserRepoImp
    ): UserRepo

    @Binds
    @Singleton
    abstract fun bindFeedRepo(
        feedRepoImp: FeedRepoImp
    ): FeedRepo

    @Binds
    @Singleton
    abstract fun bindDataRepo(
        dataRepoImp: DataRepoImp
    ): DataRepo

    @Binds
    @Singleton
    abstract fun bindSocialRepo(
        socialRepoImp: SocialRepoImp
    ): SocialRepo
}