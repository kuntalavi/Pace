package com.rk.pace.presentation.screens.user_profile

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.User

data class UserProfileUiState(
    val user: User,
    val runs: List<Run>,
    val isFollowed: Boolean,
    val followJ: Boolean = false,
    val isCurrentUser: Boolean = false
)
