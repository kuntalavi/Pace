package com.rk.pace.domain.model

import java.util.UUID

data class User(
    val userId: String = UUID.randomUUID().toString(),
    val username: String,
    val name: String,
    val email: String,
    val photoURL: String?,
    val photoURI: String?, //
    val followers: Int,
    val following: Int
)

// BIO
// HEIGHT, WEIGHT, GENDER, DATE OF BIRTH, LOCATION