package com.rk.pace.auth.presentation

sealed class AuthState {
    object Empty : AuthState()
    object Load : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}