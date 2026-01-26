package com.rk.pace.data.remote.source

import com.google.firebase.firestore.FirebaseFirestore
import com.rk.pace.data.remote.dto.FollowerDto
import com.rk.pace.data.remote.dto.UserDto
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseSocialDataSource @Inject constructor(
    firestore: FirebaseFirestore,
    private val userDataSource: FirebaseUserDataSource
) {

    val followersCollection = firestore.collection("followers")

    suspend fun isUserFollowedByCurrentUser(userId: String): Boolean {
        return try {
            val currentUserId = userDataSource.getCurrentUserId()
                ?: return false
            val documentId = "${currentUserId}_$userId"

            val snapshot = followersCollection
                .document(documentId)
                .get()
                .await()

            snapshot.exists()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getFollowers(userId: String): List<UserDto> {
        return try {
            val followersIds = getFollowersUserIds(userId)
            val userDtos = userDataSource.getUsersByIds(followersIds)
            return userDtos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFollowing(userId: String): List<UserDto> {
        return try {
            val followingIds = getFollowingUserIds(userId)
            val userDtos = userDataSource.getUsersByIds(followingIds)
            return userDtos
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun followUser(userId: String): Boolean {
        return try {
            val currentUserId = userDataSource.getCurrentUserId()
                ?: return false
            val newfollowerDto = FollowerDto(
                followerId = currentUserId,
                followingId = userId,
                createdAt = System.currentTimeMillis()
            )
            val documentId = "${currentUserId}_$userId"
            followersCollection
                .document(documentId)
                .set(newfollowerDto)
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun unFollowUser(userId: String): Boolean {
        return try {
            val currentUserId = userDataSource.getCurrentUserId()
                ?: return false
            val documentId = "${currentUserId}_$userId"

            followersCollection
                .document(documentId)
                .delete()
                .await()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun getFollowersUserIds(userId: String): List<String> {
        return try {
            val followersSnapshot = followersCollection
                .whereEqualTo("followingId", userId)
                .get()
                .await()

            followersSnapshot.documents.mapNotNull { document ->
                document.toObject(FollowerDto::class.java)?.followerId
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getFollowingUserIds(userId: String): List<String> {
        return try {

            val followersSnapshot = followersCollection
                .whereEqualTo("followerId", userId)
                .get()
                .await()

            followersSnapshot.documents.mapNotNull { document ->
                document.toObject(FollowerDto::class.java)?.followingId
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}