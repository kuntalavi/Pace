package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import javax.inject.Inject

class GetRunWithPathByUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    suspend operator fun invoke(runId: String): RunWithPath? = runRepo.getRunWithPathByRunId(runId)
}