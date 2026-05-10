package com.rk.pace.domain.ut

sealed interface AuthState {
    data object Load : AuthState
    data object Authenticated : AuthState
    data object Unauthenticated : AuthState
}