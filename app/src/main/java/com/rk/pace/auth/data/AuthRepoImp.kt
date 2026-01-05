package com.rk.pace.auth.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rk.pace.auth.domain.AuthRepo
import com.rk.pace.data.remote.dto.UserDto
import com.rk.pace.data.room.dao.UserDao
import com.rk.pace.data.room.entity.UserEntity
import com.rk.pace.domain.model.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepoImp @Inject
constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao
) : AuthRepo {

    override val user: User?
        get() = auth.currentUser?.let {
            User(
                userId = it.uid,
                name = it.displayName ?: "",
                photoURI = it.photoUrl?.toString()
            )
        }

    override val authState: Flow<User?> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser?.let {
                User(
                    userId = it.uid,
                    name = it.displayName ?: "",
                    photoURI = it.photoUrl?.toString()

                )
            }
            trySend(user)
        }
        auth.addAuthStateListener(listener)
        awaitClose { auth.removeAuthStateListener(listener) }
    }

    override suspend fun signIn(
        em: String,
        password: String
    ): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(em, password).await()
            val user = result.user ?: throw Exception("Auth F")

            val userRoom = userDao.getUser()

            if (userRoom == null) {
                val snapshot = firestore.collection("users").document(user.uid).get().await()

                if (snapshot.exists()) {
                    val name = snapshot.getString("name") ?: "User"
                    val photoUrl = snapshot.getString("photoUrl")

                    userDao.insertUser(
                        UserEntity(
                            userId = user.uid,
                            name = name,
                            photoURI = photoUrl
                        )
                    )
                }
            }

            Result.success(
                User(
                    user.uid,
                    user.displayName ?: "",
                    user.photoUrl?.toString()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signUp(
        name: String,
        em: String,
        password: String
    ): Result<User> {
        return try {

            val result = auth.createUserWithEmailAndPassword(em, password).await()
            val user = result.user ?: throw Exception("Auth F")

            val userDto = UserDto(
                userId = user.uid,
                name = name,
                photoURL = null
            )
            firestore.collection("users").document(user.uid).set(userDto).await()

            userDao.insertUser(
                UserEntity(
                    userId = user.uid,
                    name = name,
                    photoURI = null
                )
            )

            Result.success(
                User(
                    userId = user.uid,
                    name = name,
                    photoURI = null
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun sendPasswordResetEm(em: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(em).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut() {
        auth.signOut()
        userDao.removeAUsers()  // CLEARING THE ROOM DB
    }
}