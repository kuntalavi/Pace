package com.rk.pace.auth.domain.model

import com.rk.pace.domain.model.User

sealed class AuthResult {
    data class Success(val user: User) : AuthResult()
    data class Error(val message: String) : AuthResult()
}