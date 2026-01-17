package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class DeleteRunUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(run: Run) = runRepo.removeRun(run)
}