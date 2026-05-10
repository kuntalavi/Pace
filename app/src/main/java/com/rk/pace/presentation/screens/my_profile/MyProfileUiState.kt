package com.rk.pace.presentation.screens.my_profile

import com.rk.pace.domain.model.User

data class MyProfileUiState(

    val load: Boolean = false,
    val user: User,
    val error: String? = null,

    val savg: Boolean = false
)
