package com.rk.pace.presentation.screens.my_profile

import com.rk.pace.domain.model.User

sealed class MyProfileState {
    object Load: MyProfileState()
    data class Success(val user: User): MyProfileState()
    data class Error(val message: String): MyProfileState()
}