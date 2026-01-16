package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo

class IsUserLoggedInUseCase(
    private val authRepo: AuthRepo
) {
    operator fun invoke(): Boolean {
        return authRepo.isUserLoggedIn()
    }
}