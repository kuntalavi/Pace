package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.domain.model.User
import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Result<User, AuthError.NetWork> {

        return authRepo.signInWithEmail(
            email,
            password
        )

    }
}