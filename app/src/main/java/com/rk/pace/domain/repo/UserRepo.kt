package com.rk.pace.domain.repo

import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {
    fun searchUser(query: String): Flow<List<User>>
    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun updateUserProfile(user: User)
    suspend fun updateMyProfile(user: User)
    suspend fun updateLocalUser(user: User)
    suspend fun incrementFollowerCount(userId: String, amount: Long = 1)
    suspend fun incrementFollowingCount(userId: String, amount: Long = 1)
    suspend fun decrementFollowerCount(userId: String, amount: Long = 1)
    suspend fun decrementFollowingCount(userId: String, amount: Long = 1)
    suspend fun getMyProfile(): Result<User>
    suspend fun observeUserProfile(userId: String): Flow<Result<User>>
    suspend fun getLikedByUsersByUsersIds(usersIds: List<String>): Result<List<User>>
}