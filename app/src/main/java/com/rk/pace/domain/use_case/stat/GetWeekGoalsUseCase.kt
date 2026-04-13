package com.rk.pace.domain.use_case.stat

import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.repo.StatRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeekGoalsUseCase @Inject constructor(
    private val statRepo: StatRepo
) {
    operator fun invoke(): Flow<WeekGoals> {
        return statRepo.getWeekGoals()
    }
}