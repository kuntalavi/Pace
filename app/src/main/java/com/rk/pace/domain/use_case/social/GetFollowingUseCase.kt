package com.rk.pace.domain.use_case.social

import com.rk.pace.domain.model.User
import com.rk.pace.domain.repo.SocialRepo
import javax.inject.Inject

class GetFollowingUseCase @Inject constructor(
    private val socialRepo: SocialRepo
) {
    suspend operator fun invoke(userId: String): List<User> {
        return socialRepo.getFollowing(userId)
    }
}