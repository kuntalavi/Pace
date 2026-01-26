package com.rk.pace.auth.data

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.model.AuthState
import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.mapper.toDto
import com.rk.pace.data.mapper.toEntity
import com.rk.pace.data.remote.dto.UserDto
import com.rk.pace.data.room.dao.UserDao
import com.rk.pace.data.ut.InternalStorageHelper
import com.rk.pace.di.IoDispatcher
import com.rk.pace.domain.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AuthRepoImp @Inject
constructor(
    private val auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    private val userDao: UserDao,
    private val internalStorageHelper: InternalStorageHelper,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepo {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    private val usersCollection = firestore.collection("users")

    private suspend fun checkUsernameExists(username: String): Boolean {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("query", username.lowercase())
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun signUpWithEmail(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String?
    ): AuthResult = withContext(ioDispatcher) {
        try {
            val usernameExists = checkUsernameExists(username)
            if (usernameExists) {
                return@withContext AuthResult.Error("Username Is Already Taken")
            }

            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user

            if (firebaseUser != null) {
                val user = User(
                    userId = firebaseUser.uid,
                    username = username.lowercase(),
                    name = name,
                    email = email,
                    photoURL = "",
                    photoURI = photoURI, // work here
                    followers = 0,
                    following = 0,
                )
                val userDto = user.toDto()
                usersCollection.document(firebaseUser.uid).set(userDto).await()

                userDao.insertUser(user.toEntity())

                AuthResult.Success(user)
            } else {
                AuthResult.Error("")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "")
        }
    }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): AuthResult = withContext(ioDispatcher) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            if (firebaseUser != null) {
                val userDto = usersCollection.document(firebaseUser.uid)
                    .get()
                    .await()
                    .toObject(UserDto::class.java)

                if (userDto != null) {
                    var photoURI: String? = null
                    if (userDto.photoURL != null) {
                        val localPath =
                            internalStorageHelper.downloadSupabaseImageToInternalStorage(
                                userDto.photoURL,
                                userDto.userId
                            )
                        if (localPath != null) {
                            photoURI = Uri.fromFile(File(localPath)).toString()
                        }
                    }
                    val user = userDto.toDomain(
                        photoURI = photoURI
                    )

                    userDao.insertUser(user.toEntity())

                    AuthResult.Success(user)
                } else {
                    AuthResult.Error("MyProfile Not Found")
                }
            } else {
                AuthResult.Error("Authentication Failed")
            }
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "")
        }
    }

    override suspend fun signOut(): Result<Unit> = withContext(ioDispatcher) {
        try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> = withContext(ioDispatcher) {
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override fun observeAuthState(): Flow<AuthState> {
        TODO("Not yet implemented")
    }

}