package com.rk.pace.domain.use_case.user

import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.UserRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveMyProfileUseCase @Inject constructor(
    private val userRepo: UserRepo
) {
    operator fun invoke(): Flow<User?> {
        return userRepo.observeMyProfile()
    }
}