package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class GetUserRunsUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(userId: String): List<Run> {
        return runRepo.getUserRuns(userId)
    }
}