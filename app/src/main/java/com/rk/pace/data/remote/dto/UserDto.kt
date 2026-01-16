package com.rk.pace.data.remote.dto

data class UserDto(
    val userId: String = "",
    val username: String = "",
    val name: String = "",
    val email: String = "",
    val photoURL: String? = null, // Remote Firebase Firestore URL
    val followers: Int = 0,
    val following: Int = 0
)
