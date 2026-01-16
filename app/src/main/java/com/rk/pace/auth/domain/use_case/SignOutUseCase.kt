package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(): Result<Unit> {
        return authRepo.signOut()
    }
}