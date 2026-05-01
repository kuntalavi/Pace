package com.rk.pace.auth.domain.use_case

import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import javax.inject.Inject

class ValidateSignInDataUseCase @Inject constructor() {

    private val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    operator fun invoke(
        email: String,
        password: String,
    ): Result<Unit, AuthError.Validation> {

        if (email.isBlank()) return Result.Error(AuthError.Validation.EMAIL_EMPTY)
        if (!email.matches(regex)) return Result.Error(AuthError.Validation.INVALID_EMAIL)

        if (password.isBlank()) return Result.Error(AuthError.Validation.PASSWORD_EMPTY)

        return Result.Success(Unit)
    }
}