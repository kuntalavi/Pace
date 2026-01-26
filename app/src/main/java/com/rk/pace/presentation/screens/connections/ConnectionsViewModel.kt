package com.rk.pace.presentation.screens.connections

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.domain.use_case.social.GetFollowersUseCase
import com.rk.pace.domain.use_case.social.GetFollowingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConnectionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFollowersUseCase: GetFollowersUseCase,
    private val getFollowingUseCase: GetFollowingUseCase
) : ViewModel() {
    val userId: String = checkNotNull(savedStateHandle["userId"])
    val tab: Int = savedStateHandle["tab"] ?: 0

    private val _state: MutableStateFlow<ConnectionsState> = MutableStateFlow(ConnectionsState())
    val state = _state.asStateFlow()

    fun getFollowers() {
        if (state.value.isFollowersLoaded) return
        val userId = this.userId
        viewModelScope.launch {
            val followers = getFollowersUseCase(userId)
            _state.update {
                it.copy(
                    followers = followers,
                    isFollowersLoaded = true
                )
            }
        }
    }

    fun getFollowing() {
        if (state.value.isFollowingLoaded) return
        val userId = this.userId
        viewModelScope.launch {
            val following = getFollowingUseCase(userId)
            _state.update {
                it.copy(
                    following = following,
                    isFollowingLoaded = true
                )
            }
        }
    }
}