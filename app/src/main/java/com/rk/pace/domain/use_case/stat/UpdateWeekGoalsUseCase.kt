package com.rk.pace.domain.use_case.stat

import com.rk.pace.domain.model.WeekGoals
import com.rk.pace.domain.repo.StatRepo
import javax.inject.Inject

class UpdateWeekGoalsUseCase @Inject constructor(
    private val statRepo: StatRepo
) {
    suspend operator fun invoke(weekGoals: WeekGoals) {
        statRepo.updateWeekGoals(weekGoals)
    }
}