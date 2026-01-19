package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.repo.AuthRepo
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val authRepo: AuthRepo
) {
    operator fun invoke(): String? = authRepo.currentUserId
}