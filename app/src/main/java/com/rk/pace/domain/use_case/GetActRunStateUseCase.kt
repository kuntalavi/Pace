package com.rk.pace.domain.use_case

import com.rk.pace.domain.model.ActRunState
import com.rk.pace.domain.tracking.TrackerManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetActRunStateUseCase @Inject constructor(
    private val trackingManager: TrackerManager
) {
    operator fun invoke(): Flow<ActRunState> {
        return trackingManager.actRunState
    }
}