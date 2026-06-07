package com.rk.pace.domain.use_case.feed

import com.rk.pace.domain.repo.FeedRepo
import javax.inject.Inject

class SwitchPostLieUseCase @Inject constructor(
    private val repo: FeedRepo
) {
    suspend operator fun invoke(
        postId: String,
        currentUserId: String,
        isLiedByMe: Boolean
    ): Boolean {
        return if (isLiedByMe) {
            repo.unlikePost(
                postId,
                currentUserId
            )
        } else {
            repo.likePost(
                postId,
                currentUserId
            )
        }
    }
}