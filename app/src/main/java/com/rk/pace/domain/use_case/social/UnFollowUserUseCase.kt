package com.rk.pace.domain.use_case.social

import com.rk.pace.domain.repo.SocialRepo
import com.rk.pace.domain.repo.UserRepo
import javax.inject.Inject

class UnFollowUserUseCase @Inject constructor(
    private val socialRepo: SocialRepo,
    private val userRepo: UserRepo
) {
    suspend operator fun invoke(targetUserId: String): Boolean {
        return try {
            val success = socialRepo.unFollowUser(targetUserId)
            if (!success) return false

            userRepo.decrementFollowerCount(targetUserId)

            val currentUser = userRepo.getMyProfile().getOrNull()
            if (currentUser != null) {
                userRepo.decrementFollowingCount(currentUser.userId)
                userRepo.updateLocalUser(
                    currentUser.copy(
                        following = currentUser.following - 1
                    )
                )
            }

            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}