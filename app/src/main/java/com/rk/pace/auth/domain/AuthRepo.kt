package com.rk.pace.auth.domain

import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val user: User?
    val authState: Flow<User?>

    suspend fun signIn(em: String, password: String): Result<User>
    suspend fun signUp(name: String, em: String, password: String): Result<User>
    suspend fun signOut()

    suspend fun sendPasswordResetEm(em: String): Result<Unit>
}