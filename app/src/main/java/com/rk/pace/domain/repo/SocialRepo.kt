package com.rk.pace.domain.repo

import com.rk.pace.domain.model.User

interface SocialRepo {
    suspend fun isUserFollowedByCurrentUser(userId: String): Boolean
    suspend fun followUser(userId: String): Boolean
    suspend fun unFollowUser(userId: String): Boolean
    suspend fun getFollowers(userId: String): List<User>
    suspend fun getFollowing(userId: String): List<User>
}