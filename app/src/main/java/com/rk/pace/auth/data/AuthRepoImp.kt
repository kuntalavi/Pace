package com.rk.pace.auth.data

import android.net.Uri
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
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
import com.rk.pace.domain.ut.AuthError
import com.rk.pace.domain.ut.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class AuthRepoImp @Inject constructor(
    private val auth: FirebaseAuth,
    firestore: FirebaseFirestore,
    private val userDao: UserDao,
    private val internalStorageHelper: InternalStorageHelper,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AuthRepo {

    override val currentUserId: String?
        get() = auth.currentUser?.uid

    override fun observeAuthState(): Flow<AuthState> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                trySend(AuthState.Authenticated)
            } else {
                trySend(AuthState.Unauthenticated)
            }
        }

        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    private val usersCollection = firestore.collection("users")

    private suspend fun checkUsernameExists(username: String): Boolean {
        val querySnapshot = usersCollection
            .whereEqualTo("username", username.lowercase())
            .limit(1)
            .get()
            .await()

        return !querySnapshot.isEmpty
    }

    override suspend fun signUpWithEmail(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String?
    ): Result<User, AuthError.NetWork> =
        withContext(ioDispatcher) {
            try {

                val usernameExists = checkUsernameExists(username)
                if (usernameExists) {
                    return@withContext Result.Error(AuthError.NetWork.USERNAME_ALREADY_EXISTS)
                }

                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val firebaseUser =
                    result.user ?: return@withContext Result.Error(
                        AuthError.NetWork.UNKNOWN_SERVER_ERROR
                    )

                val user = User(
                    userId = firebaseUser.uid,
                    username = username.lowercase(),
                    name = name,
                    email = email,
                    photoURL = null, // work here
                    photoURI = photoURI,
                    followers = 0,
                    following = 0,
                )

                try {
                    val userDto = user.toDto()
                    usersCollection
                        .document(firebaseUser.uid)
                        .set(userDto)
                        .await()

                    userDao.insertUser(user.toEntity())

                } catch (e: Exception) {
                    try {
                        firebaseUser
                            .delete()
                            .await()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    throw e
                }

                Result.Success(user)

            } catch (e: FirebaseNetworkException) {
                Result.Error(AuthError.NetWork.NO_INTERNET)
            } catch (e: FirebaseAuthUserCollisionException) {
                Result.Error(AuthError.NetWork.USER_ALREADY_EXISTS)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(AuthError.NetWork.UNKNOWN_SERVER_ERROR)
            }
        }

    override suspend fun signInWithEmail(
        email: String,
        password: String
    ): Result<User, AuthError.NetWork> =
        withContext(ioDispatcher) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser =
                    result.user ?: return@withContext Result.Error(
                        AuthError.NetWork.USER_NOT_FOUND
                    )

                val userDto = usersCollection.document(firebaseUser.uid)
                    .get()
                    .await()
                    .toObject(UserDto::class.java)
                    ?: return@withContext Result.Error(AuthError.NetWork.USER_NOT_FOUND)

                var photoURI: String? = null
                if (!userDto.photoURL.isNullOrBlank()) {
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

                Result.Success(user)

            } catch (e: FirebaseNetworkException) {
                Result.Error(AuthError.NetWork.NO_INTERNET)
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Result.Error(AuthError.NetWork.INVALID_CRED)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(AuthError.NetWork.UNKNOWN_SERVER_ERROR)
            }
        }

    override suspend fun signOut(): Result<Unit, AuthError.NetWork> =
        withContext(ioDispatcher) {
            try {
                auth.signOut()
                Result.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(AuthError.NetWork.UNKNOWN_SERVER_ERROR)
            }
        }

}