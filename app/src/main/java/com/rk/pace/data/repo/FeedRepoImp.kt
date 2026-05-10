package com.rk.pace.data.repo

import androidx.compose.ui.util.fastFirstOrNull
import com.rk.pace.data.mapper.toDomain
import com.rk.pace.data.remote.source.FirebaseRunDataSource
import com.rk.pace.data.remote.source.FirebaseUserDataSource
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.FeedRepo
import javax.inject.Inject

class FeedRepoImp @Inject constructor(
    private val firebaseRunDataSource: FirebaseRunDataSource,
    private val firebaseUserDataSource: FirebaseUserDataSource
) : FeedRepo {

    override suspend fun getFeed(): Result<List<FeedPost>> {

        return try {

            val currentUserId = firebaseUserDataSource
                .getCurrentUserId()

            val followingIds =
                firebaseUserDataSource.getFollowingUserIds(currentUserId) + currentUserId

            val runsDtos = firebaseRunDataSource.getRunsByUserIds(followingIds)

            val sorted = runsDtos
                .sortedByDescending { it.timestamp }
                .take(20)
                .map { it.toDomain() }

            val uniqueUserIds = sorted
                .map { it.userId }
                .distinct()


            val users = if (uniqueUserIds.isNotEmpty()) {
                firebaseUserDataSource
                    .getUsersByIds(uniqueUserIds)
                    .map {
                        it.toDomain(
                            photoURI = null
                        )
                    }
            } else emptyList()

            val list = sorted
                .map { run ->

                    val user = users.fastFirstOrNull { user ->
                        user.userId == run.userId
                    }

                    FeedPost(
                        run = run,
                        user = user ?: User(
                            userId = "",
                            username = "",
                            name = "",
                            email = "",
                            photoURL = "",
                            photoURI = "",
                            followers = 0,
                            following = 0
                        ),
                        isLikedByMe = run
                            .likedBy
                            .contains(currentUserId)
                    )
                }

            Result.success(list)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun likePost(postId: String, currentUserId: String) {
        firebaseRunDataSource.likeRun(postId, currentUserId)
    }

    override suspend fun unlikePost(postId: String, currentUserId: String) {
        firebaseRunDataSource.unlikeRun(postId, currentUserId)
    }

}