package com.rk.pace.auth.presentation

sealed interface AuthUiEvent {
    data class Error(val message: String): AuthUiEvent
}