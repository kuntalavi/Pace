package com.rk.pace.presentation.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.use_case.feed.GetFeedUseCase
import com.rk.pace.domain.use_case.feed.ToggleLikePostUseCase
import com.rk.pace.domain.use_case.user.GetLikedByUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getFeedUseCase: GetFeedUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val toggleLikePostUseCase: ToggleLikePostUseCase,
    private val getLikedByUsersUseCase: GetLikedByUsersUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<FeedState> = MutableStateFlow(FeedState())
    val state = _state

    init {
        getFeed()
    }

    private fun getFeed() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isInitialLoad = true
                )
            }
            getFeedUseCase().collect { result ->
                result.fold(
                    onSuccess = { posts ->
                        _state.update {
                            it.copy(
                                posts = posts,
                                isInitialLoad = false
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                error = e.message,
                                isInitialLoad = false
                            )
                        }
                    }
                )
            }
        }
    }

    fun refreshFeed() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRefreshing = true
                )
            }
            getFeedUseCase().collect { result ->
                result.fold(
                    onSuccess = { posts ->
                        _state.update {
                            it.copy(
                                posts = posts,
                                isRefreshing = false
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                error = e.message,
                                isInitialLoad = false
                            )
                        }
                    }
                )
            }
        }
    }

    fun toggleLike(post: FeedPost) {
        val currentUserId = getCurrentUserIdUseCase() ?: return
        _state.update { state ->
            state.copy(
                posts = state.posts.map { p ->
                    if (post.run.runId == p.run.runId) {
                        p.copy(
                            isLikedByMe = !post.isLikedByMe,
                            run = p.run.copy(
                                likes = if (post.isLikedByMe) {
                                    p.run.likes - 1
                                } else p.run.likes + 1,
                                likedBy = if (post.isLikedByMe) {
                                    p.run.likedBy.filter { it != currentUserId }
                                } else p.run.likedBy + currentUserId
                            )
                        )
                    } else {
                        p
                    }
                }
            )
        }
        viewModelScope.launch {
            toggleLikePostUseCase(post.run.runId, currentUserId, post.isLikedByMe)
        }
    }

    fun clearLikedByUsers(){
        _state.update { state ->
            state.copy(
                likedByUsers = emptyList()
            )
        }
    }

    fun getLikedByUsers(likedByUserId: List<String>){
        _state.update {
            it.copy(
                isLikedByUsersLoading = true
            )
        }
        viewModelScope.launch {
            getLikedByUsersUseCase(likedByUserId).fold(
                onSuccess = { users ->
                    _state.update { state ->
                        state.copy(
                            isLikedByUsersLoading = false,
                            likedByUsers = users
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLikedByUsersLoading = false
                        )
                    }
                }
            )
        }
    }
}