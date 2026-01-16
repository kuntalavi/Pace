package com.rk.pace.auth.domain.use_case

import com.rk.pace.auth.domain.model.AuthState
import com.rk.pace.auth.domain.repo.AuthRepo
import kotlinx.coroutines.flow.Flow

class ObserveAuthStateUseCase(
    private val authRepo: AuthRepo
) {
    operator fun invoke(): Flow<AuthState> {
        return authRepo.observeAuthState()
    }
}