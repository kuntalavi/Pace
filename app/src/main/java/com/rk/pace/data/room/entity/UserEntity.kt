package com.rk.pace.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//enum class GENDER {
//    MALE, FEMALE, OTHER
//}

@Entity(
    tableName = "users"
)
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val userId: String,
    val username: String,
    val name: String,
    val email: String,
    val photoURI: String?, //
    val followers: Int,
    val following: Int
)
