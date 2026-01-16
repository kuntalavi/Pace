package com.rk.pace.data.remote.dto

data class FollowerDto(
    val followerId: String = "",
    val followingId: String = "",
    val createdAt: Long = 0L
)