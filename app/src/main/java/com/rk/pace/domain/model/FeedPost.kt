package com.rk.pace.domain.model

data class FeedPost(
    val run: Run,
    val user: User,
    val isLikedByMe: Boolean
)
