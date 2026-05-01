package com.rk.pace.auth.domain.repo

import com.rk.pace.auth.domain.model.AuthState
import com.rk.pace.domain.model.User
import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val currentUserId: String?
    fun observeAuthState(): Flow<AuthState>

    suspend fun signUpWithEmail(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String? = null
    ): Result<User, AuthError.NetWork>

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<User, AuthError.NetWork>

    suspend fun signOut(): Result<Unit, AuthError.NetWork>

    // signInWithGoogle(idToken: String): AuthResult
    //

    // FOR PROFILE SCREEN REPO
//suspend fun deleteAccount(): Result<Unit>
//
//    suspend fun updatePassword(
//        currentPassword: String,
//        newPassword: String
//    ): Result<Unit>
}