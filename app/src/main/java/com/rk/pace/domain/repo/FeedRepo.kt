package com.rk.pace.domain.repo

import com.rk.pace.domain.model.FeedPost

interface FeedRepo {
    suspend fun getFeed(): Result<List<FeedPost>>
    suspend fun likePost(postId: String, currentUserId: String): Boolean
    suspend fun unlikePost(postId: String, currentUserId: String): Boolean
}