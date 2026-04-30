package com.rk.pace.auth.domain.model

sealed interface AuthState {
    data object Load : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}