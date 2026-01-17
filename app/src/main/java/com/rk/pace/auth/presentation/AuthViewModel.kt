package com.rk.pace.auth.presentation

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.repo.AuthRepo
import com.rk.pace.auth.domain.use_case.SignInWithEmailUseCase
import com.rk.pace.auth.domain.use_case.SignOutUseCase
import com.rk.pace.auth.domain.use_case.SignUpWithEmailUseCase
import com.rk.pace.common.extension.restartApp
import com.rk.pace.domain.repo.RunRepo
import com.rk.pace.presentation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val authRepo: AuthRepo,
    private val runRepo: RunRepo,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase,
    private val signOutUseCase: SignOutUseCase
//    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
//    private val observeAuthStateUseCase: ObserveAuthStateUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthUIState>(AuthUIState.Empty)
    val authState = _authState

//    val authStateFlow = observeAuthStateUseCase().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000L),
//        initialValue = if (isUserLoggedInUseCase()) AuthState.Authenticated else AuthState.Unauthenticated
//    )

    private val _startDestination = MutableStateFlow<Route?>(null)
    val startDestination = _startDestination

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val currentUserId = authRepo.currentUserId

            if (currentUserId != null) {
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
            AuthUIState.Empty
        }
    }

    fun signUp(
        name: String,
        username: String,
        email: String,
        password: String,
        photoURI: String? = null
    ) {
        viewModelScope.launch {
            _authState.update {
                AuthUIState.Load
            }
            when (val result = signUpWithEmailUseCase(name, username, email, password, photoURI)) {
                is AuthResult.Success -> {
                    _authState.update {
                        AuthUIState.Success(result.user)
                    }
                }

                is AuthResult.Error -> {
                    _authState.update {
                        AuthUIState.Error(result.message)
                    }
                }
            }
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _authState.update {
                AuthUIState.Load
            }

            when (val result = signInWithEmailUseCase(email, password)) {
                is AuthResult.Success -> {
                    runRepo.restoreRuns(result.user.userId)
                    _authState.update {
                        AuthUIState.Success(result.user)
                    }
                }

                is AuthResult.Error -> {
                    _authState.update {
                        AuthUIState.Error(result.message)
                    }
                }
            }

        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            //
        }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase().fold(
                onSuccess = {
                    resetState()
                    restartApp(context)
                },
                onFailure = { error ->
                    _authState.update {
                        AuthUIState.Error(error.message ?: "")
                    }
                }
            )
        }
    }
}