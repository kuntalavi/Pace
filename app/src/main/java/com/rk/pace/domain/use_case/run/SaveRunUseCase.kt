package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class SaveRunUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(run: RunWithPath) {
        runRepo.saveRun(run)
    }
}