package com.rk.pace.domain.repo

import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepo {

    suspend fun getUserProfile(userId: String): Result<User>
    suspend fun updateProfile(user: User)
    suspend fun getMyProfile(): Result<User>
    suspend fun observeUserProfile(userId: String): Flow<Result<User>>
}