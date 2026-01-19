package com.rk.pace.data.repo

import com.rk.pace.data.remote.source.FirebaseFeedDataSource
import com.rk.pace.data.remote.source.FirebaseRunDataSource
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.FeedRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FeedRepoImp @Inject constructor(
    private val firebaseFeedDataSource: FirebaseFeedDataSource,
    private val firebaseRunDataSource: FirebaseRunDataSource
) : FeedRepo {
    override fun getFeed(): Flow<Result<List<FeedPost>>> {
        return firebaseFeedDataSource.getFeed()
    }

    override suspend fun likePost(postId: String, currentUserId: String) {
        firebaseRunDataSource.likeRun(postId, currentUserId)
    }

    override suspend fun unlikePost(postId: String, currentUserId: String) {
        firebaseRunDataSource.unlikeRun(postId, currentUserId)
    }

    override fun searchUser(username: String): Flow<List<User>> {
        TODO("Not yet implemented")
    }
}