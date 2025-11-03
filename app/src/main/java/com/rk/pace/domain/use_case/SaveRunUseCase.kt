package com.rk.pace.domain.use_case

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class SaveRunUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(run: Run) {
        runRepo.insertRun(run)
    }
}