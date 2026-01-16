package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.repo.AuthRepo
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
    ): AuthResult {

        //validation
//        if (email.isBlank() || password.isBlank() || username.isBlank() || name.isBlank() || )

        return authRepo.signUpWithEmail(name, username, email, password, photoURI)
    }
}