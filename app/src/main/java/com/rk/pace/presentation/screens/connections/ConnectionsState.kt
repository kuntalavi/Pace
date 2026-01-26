package com.rk.pace.presentation.screens.connections

import com.rk.pace.domain.model.User

data class ConnectionsState(
    val followers: List<User> = emptyList(),
    val following: List<User> = emptyList(),
    val isFollowersLoaded: Boolean = false,
    val isFollowingLoaded: Boolean = false
)