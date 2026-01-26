package com.rk.pace.presentation.screens.user_profile

sealed class UserProfileState {
    class Load : UserProfileState()
    class Success(val userProfileUiState: UserProfileUiState): UserProfileState()
    class Error(val message: String): UserProfileState()
}