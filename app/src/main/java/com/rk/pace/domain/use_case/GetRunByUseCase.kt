package com.rk.pace.domain.use_case

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class GetRunByUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(runId: Long): RunWithPath? = runRepo.getRunById(runId)
}