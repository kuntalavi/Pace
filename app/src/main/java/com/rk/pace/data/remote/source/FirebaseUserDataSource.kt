package com.rk.pace.data.remote.source

import android.content.Context
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rk.pace.data.remote.dto.UserDto
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    firestore: FirebaseFirestore,
    private val supabase: SupabaseClient,
    private val auth: FirebaseAuth
) {
    companion object {
        private const val BUCKET_NAME = "user_dp"
    }

    private val usersCollection = firestore.collection("users")

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun incrementFollowerCount(userId: String, amount: Long = 1) {
        try {
            usersCollection
                .document(userId)
                .update("followers", FieldValue.increment(amount))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun decrementFollowerCount(userId: String, amount: Long = 1) {
        try {
            usersCollection
                .document(userId)
                .update("followers", FieldValue.increment(-amount))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun incrementFollowingCount(userId: String, amount: Long = 1) {
        try {
            usersCollection
                .document(userId)
                .update("following", FieldValue.increment(amount))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun decrementFollowingCount(userId: String, amount: Long = 1) {
        try {
            usersCollection
                .document(userId)
                .update("following", FieldValue.increment(-amount))
                .await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun searchUser(query: String): Flow<List<UserDto>> = callbackFlow {
        val normalizedQuery = query.trim().lowercase()

        val subscription = usersCollection.orderBy("username")
            .startAt(normalizedQuery)
            .endAt(normalizedQuery + "\uf8ff")
            .limit(10)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val usersDto = snapshot?.toObjects(UserDto::class.java) ?: emptyList()
                trySend(usersDto)
            }
        awaitClose { subscription.remove() }
    }

    suspend fun getUsersByIds(userIds: List<String>): List<UserDto> {
        val chunks = userIds.chunked(30)
        val allUsers = mutableListOf<UserDto>()

        return try {
            for (chunk in chunks) {
                val snapshot = usersCollection
                    .whereIn(FieldPath.documentId(), chunk)
                    .get()
                    .await()

                val usersDto = snapshot.documents.mapNotNull { document ->
                    document.toObject(UserDto::class.java)
                }
                allUsers.addAll(usersDto)
            }
            allUsers
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getUserById(userId: String): UserDto? {
        return try {
            usersCollection.document(userId)
                .get()
                .await()
                .toObject(UserDto::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun updateUserProfile(userDto: UserDto, photoURI: String?): Boolean {
        return try {
            var updatedPhotoURL = userDto.photoURL

            if (photoURI != null) {
                val fileName = "${System.currentTimeMillis()}.png"
                val filePath = "${userDto.userId}/$fileName"
                val bucket = supabase.storage.from(BUCKET_NAME)

                val bytes = context.contentResolver.openInputStream(photoURI.toUri())?.use {
                    it.readBytes()
                } ?: throw Exception("Failed to read image file")

                bucket.upload(filePath, bytes)

                val files = bucket.list(userDto.userId)

                val toDelete = files
                    .filter { it.name != fileName }
                    .map { "${userDto.userId}/${it.name}" }

                if (toDelete.isNotEmpty()) {
                    bucket.delete(toDelete)
                }

                updatedPhotoURL = bucket.publicUrl(filePath)
            }

            val newUserDto = userDto.copy(photoURL = updatedPhotoURL)

            usersCollection.document(userDto.userId)
                .set(newUserDto, SetOptions.merge())
                .await()

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}