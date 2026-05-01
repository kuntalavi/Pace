package com.rk.pace.domain.ut

sealed interface AuthError : Error {

    enum class Validation : AuthError {
        NAME_EMPTY,
        USERNAME_EMPTY,
        EMAIL_EMPTY,
        PASSWORD_EMPTY,
        CONFIRM_PASSWORD_EMPTY,
        PASSWORDS_DONT_MATCH,
        INVALID_EMAIL,
        PASSWORD_TOO_SHORT,
        PASSWORD_NO_UPPERCASE,
        PASSWORD_NO_DIGIT
    }

    enum class NetWork : AuthError {
        USERNAME_ALREADY_EXISTS,
        USER_ALREADY_EXISTS,
        INVALID_CRED,
        USER_NOT_FOUND,
        NO_INTERNET,
        UNKNOWN_SERVER_ERROR
    }

}