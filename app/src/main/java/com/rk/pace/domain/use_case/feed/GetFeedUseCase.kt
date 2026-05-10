package com.rk.pace.domain.use_case.feed

import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.repo.FeedRepo
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val repo: FeedRepo
) {
    suspend operator fun invoke(): Result<List<FeedPost>> {
        return repo.getFeed()
    }
}