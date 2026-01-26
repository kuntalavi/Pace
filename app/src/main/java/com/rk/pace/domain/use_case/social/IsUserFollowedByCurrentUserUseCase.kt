package com.rk.pace.domain.use_case.social

import com.rk.pace.domain.repo.SocialRepo
import javax.inject.Inject

class IsUserFollowedByCurrentUserUseCase @Inject constructor(
    private val socialRepo: SocialRepo
) {
    suspend operator fun invoke(userId: String): Boolean {
        return socialRepo.isUserFollowedByCurrentUser(userId)
    }
}