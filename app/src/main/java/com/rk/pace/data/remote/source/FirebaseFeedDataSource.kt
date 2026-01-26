package com.rk.pace.data.remote.source

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.remote.dto.FollowerDto
import com.rk.pace.data.remote.dto.RunDto
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseFeedDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseUserDataSource: FirebaseUserDataSource
) {

    fun getFeed(
        limit: Long = 20
    ): Flow<Result<List<FeedPost>>> = flow {
        try {
            val currentUserId = firebaseUserDataSource.getCurrentUserId()
            val feedR = mutableListOf<RunDto>()
            val currentUserRunsSnapshot = firestore.collection("runs")
                .whereEqualTo("userId",currentUserId)
                .get()
                .await()

            val currentUserRuns = currentUserRunsSnapshot.documents.mapNotNull { document ->
                document.toObject(RunDto::class.java)
            }
            feedR.addAll(currentUserRuns)

            val followingIds = getFollowingUserIds(currentUserId)

            if (followingIds.isNotEmpty()){
                followingIds.chunked(10).forEach { batch ->
                    val followUsersRunsSnapshot = firestore.collection("runs")
                        .whereIn("userId", batch) //
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .limit(limit)
                        .get()
                        .await()

                    val followUsersRuns = followUsersRunsSnapshot.documents.mapNotNull { document ->
                        document.toObject(RunDto::class.java)
                    }
                    feedR.addAll(followUsersRuns)
                }
            }

            val sortedFeedR = feedR.sortedByDescending { it.timestamp }
                .take(limit.toInt())

            val feedPosts = sortedFeedR.map { runDto ->
                val user = firebaseUserDataSource.getUserById(runDto.userId)?.toDomain(
                    photoURI = ""
                )
                val isLikedByMe = runDto.likedBy.contains(currentUserId)

                FeedPost(
                    run = runDto.toDomain(),
                    user = user ?: User( //
                        userId = "",
                        username = "",
                        name = "",
                        email = "",
                        photoURL = "",
                        photoURI = "",
                        followers = 0,
                        following = 0
                    ),
                    isLikedByMe = isLikedByMe
                )
            }
            emit(Result.success(feedPosts))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.catch { e ->
        emit(Result.failure(e))
    }

    private suspend fun getFollowingUserIds(userId: String?): List<String> {
        return try {
            if (userId == null) return emptyList()

            val followersSnapshot = firestore.collection("followers")
                .whereEqualTo("followerId", userId) // followers documents where follower is myself
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