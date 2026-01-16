package com.rk.pace.auth.domain.repo

import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepo {

    val currentUserId: String?

    suspend fun signUpWithEmail(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String? = null
    ): AuthResult

    suspend fun signInWithEmail(
        email: String,
        password: String
    ): AuthResult

    suspend fun signOut(): Result<Unit>

    suspend fun resetPassword(
        email: String
    ): Result<Unit>

    //

    fun isUserLoggedIn(): Boolean

    fun observeAuthState(): Flow<AuthState>

//    val authState: Flow<MyProfile?>
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