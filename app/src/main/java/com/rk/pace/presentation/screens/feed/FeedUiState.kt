package com.rk.pace.presentation.screens.feed

import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User

data class FeedUiState(
    val posts: List<FeedPost> = emptyList(),

    val liedByUsers: List<User> = emptyList(),
    val isLiedByUsersLoad: Boolean = false,
    val showLiesBottomSheet: Boolean = false,
    val selectedPostLies: Int = 0,

    val initialLoad: Boolean = true,
    val refresh: Boolean = false,
    val error: String? = null
)