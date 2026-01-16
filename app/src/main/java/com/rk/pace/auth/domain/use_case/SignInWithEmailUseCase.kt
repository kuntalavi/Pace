package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.repo.AuthRepo
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): AuthResult {
        return authRepo.signInWithEmail(email, password)
    }
}