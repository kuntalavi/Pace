package com.rk.pace.domain.use_case.run

import com.rk.pace.domain.model.RunWithPath
import com.rk.pace.domain.repo.RunRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetARunsWithPathUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    operator fun invoke(): Flow<List<RunWithPath>> = runRepo.getARunsWithPath()
}