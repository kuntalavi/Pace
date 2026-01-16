package com.rk.pace.data.remote.source

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.rk.pace.data.remote.dto.UserDto
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseUserDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    private val supabase: SupabaseClient,
    @param:ApplicationContext private val context: Context,
    private val auth: FirebaseAuth
) {
    companion object {
        private const val BUCKET_NAME = "user_dp"
    }

    private val usersCollection = firestore.collection("users")

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    suspend fun getUserById(userId: String): UserDto? {
        return try {
            usersCollection.document(userId)
                .get()
                .await()
                .toObject(UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(userDto: UserDto, photoURI: String?): Boolean {
        return try {
            var updatedPhotoURL = userDto.photoURL

            if (photoURI != null) {
                val fileName = "${userDto.userId}/dp.jpg"
                val bucket = supabase.storage.from(BUCKET_NAME)

                val bytes = context.contentResolver.openInputStream(photoURI.toUri())?.use {
                    it.readBytes()
                } ?: throw Exception("Failed to read image file")

                bucket.upload(fileName, bytes) {
                    upsert = true
                }

                updatedPhotoURL = bucket.publicUrl(fileName)
                Log.d("SupabaseDataSource", "Image uploaded: $updatedPhotoURL")
            }

            val newUserDto = userDto.copy(photoURL = updatedPhotoURL)

            usersCollection.document(userDto.userId)
                .set(newUserDto, SetOptions.merge())
                .await()

            true
        } catch (e: Exception) {
            Log.e("SupabaseDataSource", "Error updating user profile: ${e.message}", e)
            false
        }
    }

    suspend fun createUserProfile(userDto: UserDto): Boolean {
        return try {
            usersCollection.document(userDto.userId)
                .set(userDto)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}