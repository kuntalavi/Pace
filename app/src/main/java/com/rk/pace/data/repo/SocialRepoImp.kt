package com.rk.pace.data.repo

import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.remote.source.FirebaseSocialDataSource
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.SocialRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SocialRepoImp @Inject constructor(
    private val firebaseSocialDataSource: FirebaseSocialDataSource,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : SocialRepo {
    override suspend fun isUserFollowedByCurrentUser(userId: String): Boolean {
        return firebaseSocialDataSource.isUserFollowedByCurrentUser(userId)
    }

    override suspend fun followUser(userId: String): Boolean {
        return firebaseSocialDataSource.followUser(userId)
    }

    override suspend fun unFollowUser(userId: String): Boolean {
        return firebaseSocialDataSource.unFollowUser(userId)
    }

    override suspend fun getFollowers(userId: String): List<User> = withContext(ioDispatcher) {
        val followersDtos = firebaseSocialDataSource.getFollowers(userId)
        val followers = followersDtos.map { it.toDomain(photoURI = "") }
        return@withContext followers
    }

    override suspend fun getFollowing(userId: String): List<User> = withContext(ioDispatcher) {
        val followingDtos = firebaseSocialDataSource.getFollowing(userId)
        val following = followingDtos.map { it.toDomain(photoURI = "") }
        return@withContext following
    }
}