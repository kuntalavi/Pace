package com.rk.pace.auth.presentation

import com.rk.pace.domain.model.User

sealed class AuthUIState {
    object Empty : AuthUIState()
    object Load : AuthUIState()
    data class Success(val user: User) : AuthUIState()
    data class Error(val message: String) : AuthUIState()
}