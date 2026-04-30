package com.rk.pace.auth.presentation

data class AuthUiState(

    val load: Boolean = false,

    val name: String = "",
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val photoURI: String = ""
)
