package com.rk.pace.domain.use_case.stat

import android.os.Build
import androidx.annotation.RequiresApi
import com.rk.pace.common.ut.TimestampUt.getWeekBounds
import com.rk.pace.domain.model.Run
import com.rk.pace.domain.repo.StatRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWeekRunsUseCase @Inject constructor(
    private val statRepo: StatRepo
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(weekOffset: Int): Flow<List<Run>> = flow {
        val (start, end) = getWeekBounds(weekOffset)
        emitAll(statRepo.getWeekRuns(start, end))
    }
}