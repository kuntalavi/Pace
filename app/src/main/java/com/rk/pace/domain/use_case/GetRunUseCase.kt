package com.rk.pace.domain.use_case

import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class GetRunUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(runId: Long) = runRepo.getRunById(runId)
}