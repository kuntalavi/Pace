package com.rk.pace.auth.domain.model

sealed class AuthState {
    object Load : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}