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

    private val usersCollection = firestore.collection("users")

    private suspend fun checkUsernameExists(username: String): Boolean {
        return try {
            val querySnapshot = usersCollection
                .whereEqualTo("username", username.lowercase())
                .get()
                .await()

            !querySnapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }

    override val currentUserId: String?
        get() = auth.currentUser?.uid

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
            Result.failure(Exception("No Account Found With This Email"))
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
//    override val authState: Flow<MyProfile?> = callbackFlow {
//        val listener = FirebaseAuth.AuthStateListener { auth ->
//            val user = auth.currentUser?.let {
//                MyProfile(
//                    userId = it.uid,
//                    name = it.displayName ?: "",
//                    photoURI = it.photoUrl?.toString(),
//                    email = it.email ?: ""
//                )
//            }
//            trySend(user)
//        }
//        auth.addAuthStateListener(listener)
//        awaitClose { auth.removeAuthStateListener(listener) }
//    }
//
//    override suspend fun signIn(
//        em: String,
//        password: String
//    ): Result<MyProfile> {
//        return try {
//            val result = auth.signInWithEmailAndPassword(em, password).await()
//            val user = result.user ?: throw Exception("Auth F")
//
//            val userRoom = userDao.getUser()
//
//            if (userRoom == null) {
//                val snapshot = firestore.collection("users").document(user.uid).get().await()
//
//                if (snapshot.exists()) {
//                    val name = snapshot.getString("name") ?: "MyProfile"
//                    val photoUrl = snapshot.getString("photoUrl")
//
//                    userDao.insertUser(
//                        UserEntity(
//                            userId = user.uid,
//                            name = name,
//                            photoURI = photoUrl,
//                            email = em
//                        )
//                    )
//                }
//            }
//
//            Result.success(
//                MyProfile(
//                    user.uid,
//                    user.displayName ?: "",
//                    user.photoUrl?.toString(),
//                    email = user.email ?: ""
//                )
//            )
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun signUp(
//        name: String,
//        em: String,
//        password: String,
//        photoURL: String?
//    ): Result<MyProfile> {
//        return try {
//
//            val result = auth.createUserWithEmailAndPassword(em, password).await()
//            val user = result.user ?: throw Exception("Auth F")
//
//            val userDto = UserDto(
//                userId = user.uid,
//                name = name,
//                photoURL = photoURL,
//                email = em
//            )
//            firestore.collection("users").document(user.uid).set(userDto).await()
//
//            userDao.insertUser(
//                UserEntity(
//                    userId = user.uid,
//                    name = name,
//                    photoURI = photoURL,
//                    email = em
//                )
//            )
//
//            Result.success(
//                MyProfile(
//                    userId = user.uid,
//                    name = name,
//                    photoURI = photoURL,
//                    email = em
//                )
//            )
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
//
//    override suspend fun sendPasswordResetEm(em: String): Result<Unit> {
//        return try {
//            auth.sendPasswordResetEmail(em).await()
//            Result.success(Unit)
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }
}