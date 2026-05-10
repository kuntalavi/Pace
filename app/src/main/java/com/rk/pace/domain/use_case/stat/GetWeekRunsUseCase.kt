package com.rk.pace.domain.use_case.stat

import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.StatRepo
import com.rk.pace.domain.ut.WeekBoundsUt.getWeekBounds
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeekRunsUseCase @Inject constructor(
    private val statRepo: StatRepo
) {
    operator fun invoke(weekOffset: Int): Flow<List<Run>> = flow {
        val (start, end) = getWeekBounds(weekOffset)
        emitAll(statRepo.getWeekRuns(start, end))
    }
}