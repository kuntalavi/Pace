package com.rk.pace.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.model.AuthResult
import com.rk.pace.auth.domain.use_case.SignInWithEmailUseCase
import com.rk.pace.auth.domain.use_case.SignUpWithEmailUseCase
import com.rk.pace.domain.repo.RunRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val runRepo: RunRepo,
    private val signUpWithEmailUseCase: SignUpWithEmailUseCase,
    private val signInWithEmailUseCase: SignInWithEmailUseCase
) : ViewModel() {

    private var _state = MutableStateFlow(AuthUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<AuthUiEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: AuthAction) {
        when (action) {
            AuthAction.OnSignUpClick -> signUp()

            AuthAction.OnSignInClick -> signIn()

            is AuthAction.OnNameChange -> {
                _state.update {
                    it.copy(
                        name = action.name
                    )
                }
            }

            is AuthAction.OnUsernameChange -> {
                _state.update {
                    it.copy(
                        username = action.username
                    )
                }
            }

            is AuthAction.OnEmailChange -> {
                _state.update {
                    it.copy(
                        email = action.email
                    )
                }
            }

            is AuthAction.OnPasswordChange -> {
                _state.update {
                    it.copy(
                        password = action.password
                    )
                }
            }

            is AuthAction.OnConfirmPasswordChange -> {
                _state.update {
                    it.copy(
                        confirmPassword = action.confirmPassword
                    )
                }
            }
        }
    }

    private fun signUp() {
        val value = _state.value

        viewModelScope.launch {
            if (value.password != value.confirmPassword) {
                _events.send(
                    AuthUiEvent.Error(
                        "Passwords Dont Match"
                    )
                )
                return@launch
            }

            _state.update {
                it.copy(
                    load = true
                )
            }

            when (
                val result = signUpWithEmailUseCase(
                    value.name,
                    value.username,
                    value.email,
                    value.password,
                    value.photoURI
                )
            ) {
                is AuthResult.Success -> {
                    _state.update {
                        it.copy(
                            load = false
                        )
                    }
                }

                is AuthResult.Error -> {
                    _state.update {
                        it.copy(
                            load = false
                        )
                    }
                    _events.send(
                        AuthUiEvent.Error(
                            result.message
                        )
                    )
                }
            }
        }
    }

    private fun signIn() {
        val value = _state.value
        viewModelScope.launch {
            _state.update {
                it.copy(
                    load = true
                )
            }

            when (
                val result = signInWithEmailUseCase(
                    value.email,
                    value.password
                )
            ) {
                is AuthResult.Success -> {
                    runRepo.restoreRuns(result.user.userId)
                    _state.update {
                        it.copy(
                            load = false
                        )
                    }
                }

                is AuthResult.Error -> {
                    _state.update {
                        it.copy(
                            load = false
                        )
                    }
                    _events.send(
                        AuthUiEvent.Error(
                            result.message
                        )
                    )
                }
            }

        }
    }

}