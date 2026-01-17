package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.domain.repo.DataRepo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepo: AuthRepo,
    private val dataRepo: DataRepo
) {
    suspend operator fun invoke(): Result<Unit> {
        dataRepo.deleteUserData()
        return authRepo.signOut()
    }
}