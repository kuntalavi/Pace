package com.rk.pace.domain.repo

import com.rk.pace.domain.model.FeedPost
import kotlinx.coroutines.flow.Flow

interface FeedRepo {

    fun getFeed(): Flow<Result<List<FeedPost>>>
    suspend fun likePost(postId: String, currentUserId: String)
    suspend fun unlikePost(postId: String, currentUserId: String)
}