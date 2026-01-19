package com.rk.pace.domain.use_case.feed

import com.rk.pace.domain.repo.FeedRepo
import javax.inject.Inject

class ToggleLikePostUseCase @Inject constructor(
    private val feedRepo: FeedRepo
) {
    suspend operator fun invoke(postId: String, currentUserId: String, isLikedByMe: Boolean) {
        if (isLikedByMe) {
            feedRepo.unlikePost(postId, currentUserId)
        } else {
            feedRepo.likePost(postId, currentUserId)
        }
    }
}