package com.rk.pace.auth.presentation

sealed interface AuthAction {

    data class OnNameChange(val name: String) : AuthAction
    data class OnUsernameChange(val username: String) : AuthAction
    data class OnEmailChange(val email: String) : AuthAction
    data class OnPasswordChange(val password: String) : AuthAction
    data class OnConfirmPasswordChange(val confirmPassword: String) : AuthAction

    data object OnSignUpClick : AuthAction
    data object OnSignInClick : AuthAction

}