package com.rk.pace.presentation.screens.my_profile

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.User

data class MyProfileUiState(

    val load: Boolean = false,
    val user: User? = null,
    val photoUri: String? = null,
    val runs: List<Run> = emptyList(),
    val error: String? = null,

    val saving: Boolean = false
)
