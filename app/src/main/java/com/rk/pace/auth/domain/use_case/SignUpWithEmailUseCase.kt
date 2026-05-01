package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.domain.model.User
import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import javax.inject.Inject

class SignUpWithEmailUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    suspend operator fun invoke(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String? = null
    ): Result<User, AuthError.NetWork> {

        return authRepo.signUpWithEmail(
            name,
            username,
            email,
            password,
            photoURI
        )

    }
}