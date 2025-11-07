package com.rk.pace.domain.use_case

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.RunRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetARunsUseCase @Inject constructor(
    private val runRepo: RunRepo
) {
    operator fun invoke(): Flow<List<Run>> = runRepo.getARuns()
}