package com.rk.pace.presentation.screens.feed

import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User

data class FeedState(
    val posts: List<FeedPost> = emptyList(),
    val likedByUsers: List<User> = emptyList(),
    val isLikedByUsersLoading: Boolean = false,

    val isInitialLoad: Boolean = false,
    val isRefreshing: Boolean = false,

    val error: String? = null
)