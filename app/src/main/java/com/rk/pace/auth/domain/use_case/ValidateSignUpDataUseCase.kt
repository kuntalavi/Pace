package com.rk.pace.auth.domain.use_case

import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import javax.inject.Inject

class ValidateSignUpDataUseCase @Inject constructor() {

    private val regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    operator fun invoke(
        name: String,
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<Unit, AuthError.Validation> {

        if (name.isBlank()) return Result.Error(AuthError.Validation.NAME_EMPTY)
        if (username.isBlank()) return Result.Error(AuthError.Validation.USERNAME_EMPTY)

        if (email.isBlank()) return Result.Error(AuthError.Validation.EMAIL_EMPTY)
        if (!email.matches(regex)) return Result.Error(AuthError.Validation.INVALID_EMAIL)

        if (password.isBlank()) return Result.Error(AuthError.Validation.PASSWORD_EMPTY)
        if (confirmPassword.isBlank()) return Result.Error(AuthError.Validation.CONFIRM_PASSWORD_EMPTY)
        if (password != confirmPassword) return Result.Error(AuthError.Validation.PASSWORDS_DONT_MATCH)

        if (password.length < 8) return Result.Error(AuthError.Validation.PASSWORD_TOO_SHORT)
        if (!password.any { it.isUpperCase() }) return Result.Error(AuthError.Validation.PASSWORD_NO_UPPERCASE)
        if (!password.any { it.isDigit() }) return Result.Error(AuthError.Validation.PASSWORD_NO_DIGIT)

        return Result.Success(Unit)
    }

}