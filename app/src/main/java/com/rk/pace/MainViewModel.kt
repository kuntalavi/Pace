package com.rk.pace

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.model.AuthState
import com.rk.pace.auth.domain.use_case.ObserveAuthStateUseCase
import com.rk.pace.domain.tracker.TrackerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    observeAuthState: ObserveAuthStateUseCase,
    trackerManager: TrackerManager
) : ViewModel() {

    val authState: StateFlow<AuthState> = observeAuthState()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            AuthState.Load
        )

    val isRunAct = trackerManager.isAct

}