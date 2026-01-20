package com.rk.pace.domain.repo

import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    fun searchUser(query: String): Flow<List<User>>
    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun updateProfile(user: User)
    suspend fun getMyProfile(): Result<User>
    suspend fun observeUserProfile(userId: String): Flow<Result<User>>
    suspend fun getLikedByUsersByUsersIds(usersIds: List<String>): Result<List<User>>
}