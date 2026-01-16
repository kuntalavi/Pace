package com.rk.pace.domain.use_case.user

import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.UserRepo
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepo: UserRepo
) {
    suspend operator fun invoke(userId: String): Result<User>{
        return userRepo.getUserProfile(userId)
    }
}