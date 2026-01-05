package com.rk.pace.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.AuthRepo
import com.rk.pace.domain.repo.RunRepo
import com.rk.pace.presentation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepo: AuthRepo,
    private val runRepo: RunRepo
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Empty)
    val authState = _authState

    private val _startDestination = MutableStateFlow<Route?>(null)
    val startDestination = _startDestination

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus(){
        viewModelScope.launch {
            val user = authRepo.user

            if (user != null) {
                _startDestination.update {
                    Route.Root.BotNav
                }
            } else {
                _startDestination.update {
                    Route.Root.Auth
                }
            }
        }
    }


    fun resetState() {
        _authState.update {
            AuthState.Empty
        }
    }

    fun signUp(name: String, em: String, password: String) {
        viewModelScope.launch {
            _authState.update {
                AuthState.Load
            }

            val result = authRepo.signUp(name, em, password)

            result.onSuccess {
                _authState.update {
                    AuthState.Success
                }
            }
            result.onFailure { e ->
                _authState.update {
                    AuthState.Error(e.message ?: "")
                }
            }
        }
    }

    fun signIn(em: String, password: String) {
        viewModelScope.launch {
            _authState.update {
                AuthState.Load
            }

            val result = authRepo.signIn(em, password)

            result.onSuccess { user ->
                runRepo.restoreRuns(user.userId)
                _authState.update {
                    AuthState.Success
                }
            }
            result.onFailure { e ->
                _authState.update {
                    AuthState.Error(e.message ?: "")
                }
            }
        }
    }

    fun forgetPassword(em: String) {
        viewModelScope.launch {
            if (em.isBlank()) {
                _authState.update {
                    AuthState.Error("")
                }
                return@launch
            }
            _authState.update {
                AuthState.Load
            }
            val result = authRepo.sendPasswordResetEm(em)
            result.onSuccess {
                _authState.update {
                    AuthState.Error("password reset em sent to $em")
                }
            }
            result.onFailure { e ->
                _authState.update {
                    AuthState.Error(e.message ?: "")
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            authRepo.signOut()
            resetState()
        }
    }
}