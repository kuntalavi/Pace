package com.rk.pace.presentation.screens.feed

import com.rk.pace.domain.model.FeedPost

data class FeedState(
    val posts: List<FeedPost> = emptyList(),

    val isInitialLoad: Boolean = false,
    val isRefreshing: Boolean = false,

    val error: String? = null
)