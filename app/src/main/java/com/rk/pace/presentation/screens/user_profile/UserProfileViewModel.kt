package com.rk.pace.presentation.screens.user_profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.domain.use_case.run.GetUserRunsUseCase
import com.rk.pace.domain.use_case.social.FollowUserUseCase
import com.rk.pace.domain.use_case.social.IsUserFollowedByCurrentUserUseCase
import com.rk.pace.domain.use_case.social.UnFollowUserUseCase
import com.rk.pace.domain.use_case.user.GetUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getUserRunsUseCase: GetUserRunsUseCase,
    private val isUserFollowedByCurrentUSerUseCase: IsUserFollowedByCurrentUserUseCase,
    private val followUserUseCase: FollowUserUseCase,
    private val unFollowUserUseCase: UnFollowUserUseCase,
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<UserProfileState> =
        MutableStateFlow(UserProfileState.Load())
    val state = _state.asStateFlow()

    val currentUserId = getCurrentUserIdUseCase()
    val userId: String = savedStateHandle["userId"] ?: ""

    init {
        getData(userId)
    }

    fun followUser() {
        updateFollowJState(true)
        val userId = this.userId
        viewModelScope.launch {
            val success = followUserUseCase(userId)

            val currentState = _state.value
            if (currentState is UserProfileState.Success) {
                if (success) {
                    _state.update {
                        UserProfileState.Success(
                            currentState.userProfileUiState.copy(
                                isFollowed = true,
                                followJ = false,
                                user = currentState.userProfileUiState.user.copy(
                                    followers = currentState.userProfileUiState.user.followers + 1
                                )
                            )
                        )
                    }
                } else {
                    updateFollowJState(false)
                }
            }
        }
    }

    fun unFollowUser() {
        updateFollowJState(true)
        val userId = this.userId
        viewModelScope.launch {
            val success = unFollowUserUseCase(userId)
            val currentState = _state.value

            if (currentState is UserProfileState.Success) {
                if (success) {
                    _state.update {
                        UserProfileState.Success(
                            currentState.userProfileUiState.copy(
                                isFollowed = false,
                                followJ = false,
                                user = currentState.userProfileUiState.user.copy(
                                    followers = currentState.userProfileUiState.user.followers - 1
                                )
                            )
                        )
                    }
                } else {
                    updateFollowJState(false)
                }
            }
        }
    }

    private fun updateFollowJState(followJ: Boolean) {
        val currentState = _state.value
        if (currentState is UserProfileState.Success) {
            _state.update {
                UserProfileState.Success(
                    currentState.userProfileUiState.copy(followJ = followJ)
                )
            }
        }
    }

    private fun getData(userId: String) {
        _state.update {
            UserProfileState.Load()
        }
        viewModelScope.launch {
            try {
                val userDeferred = async { getUserProfileUseCase(userId) }
                val runsDeferred = async { getUserRunsUseCase(userId) }
                val isFollowedDeferred = async { isUserFollowedByCurrentUSerUseCase(userId) }

                val userResult = userDeferred.await()
                val runs = runsDeferred.await()
                val isFollowed = isFollowedDeferred.await()

                val isCurrentUser = currentUserId == userId

                userResult.fold(
                    onSuccess = { user ->
                        _state.update {
                            UserProfileState.Success(
                                UserProfileUiState(
                                    user = user,
                                    runs = runs,
                                    isFollowed = isFollowed,
                                    isCurrentUser = isCurrentUser
                                )
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            UserProfileState.Error(error.message ?: "Error")
                        }
                    }
                )

            } catch (e: Exception) {
                _state.update {
                    UserProfileState.Error(e.message ?: "Error")
                }
            }
        }
    }
}