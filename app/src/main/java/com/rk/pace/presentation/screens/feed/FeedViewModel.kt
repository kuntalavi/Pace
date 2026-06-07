package com.rk.pace.presentation.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.auth.domain.use_case.GetCurrentUserIdUseCase
import com.rk.pace.domain.model.FeedPost
import com.rk.pace.domain.use_case.feed.GetFeedUseCase
import com.rk.pace.domain.use_case.feed.SwitchPostLieUseCase
import com.rk.pace.domain.use_case.user.GetLiedByUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val getFeedUseCase: GetFeedUseCase,
    private val switchPostLieUseCase: SwitchPostLieUseCase,
    private val getLiedByUsersUseCase: GetLiedByUsersUseCase
) : ViewModel() {

    val currentUserId = getCurrentUserIdUseCase() ?: ""

    private val _state: MutableStateFlow<FeedUiState> = MutableStateFlow(FeedUiState())
    val state = _state.asStateFlow()

    private val _events = Channel<FeedEvent>()
    val events = _events.receiveAsFlow()

    init {
        getFeed()
    }

    fun onAction(action: FeedAction) {
        when (action) {
            FeedAction.DismissLiesBottomSheet -> {
                dismissLiesBottomSheet()
            }

            is FeedAction.OnPostLiesClick -> {
                showLiesBottomSheet(
                    action.post.run.likedBy
                )
            }

            is FeedAction.OnRunClick -> {
                viewModelScope.launch {
                    _events.send(
                        FeedEvent.OnRunClick(
                            action.userId,
                            action.runId
                        )
                    )
                }
            }

            is FeedAction.OnUserClick -> {
                viewModelScope.launch {
                    _events.send(
                        FeedEvent.OnUserClick(
                            action.userId
                        )
                    )
                }
            }

            FeedAction.Refresh -> {
                refresh()
            }

            is FeedAction.OnPostLieIconClick -> {
                switchPostLie(
                    action.post
                )
            }
        }
    }

    private fun getFeed() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    initialLoad = true
                )
            }
            getFeedUseCase()
                .fold(
                    onSuccess = { posts ->
                        _state.update {
                            it.copy(
                                posts = posts,
                                initialLoad = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                error = e.message,
                                initialLoad = false
                            )
                        }
                    }
                )
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    refresh = true
                )
            }
            getFeedUseCase()
                .fold(
                    onSuccess = { posts ->
                        _state.update {
                            it.copy(
                                posts = posts,
                                refresh = false,
                                error = null
                            )
                        }
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                error = e.message,
                                refresh = false
                            )
                        }
                    }
                )
        }
    }

    private fun switchPostLie(post: FeedPost) {
        switchPostLieUi(post)
        viewModelScope.launch {
            val result = switchPostLieUseCase(
                post.run.runId,
                currentUserId,
                post.isLikedByMe
            )
            if (!result) {
                switchPostLieUi(post)
            }
        }
    }

    private fun switchPostLieUi(
        post: FeedPost
    ) {
        _state.update { state ->
            state.copy(
                posts = state.posts.map { p ->
                    if (
                        post.run.runId == p.run.runId
                    ) {
                        p.copy(
                            isLikedByMe = !post.isLikedByMe,
                            run = p.run.copy(
                                likes = if (
                                    post.isLikedByMe
                                ) {
                                    p.run.likes - 1
                                } else p.run.likes + 1,
                                likedBy = if (
                                    post.isLikedByMe
                                ) {
                                    p.run.likedBy.filter {
                                        it != currentUserId
                                    }
                                } else p.run.likedBy + currentUserId
                            )
                        )
                    } else {
                        p
                    }
                }
            )
        }
    }

    private fun dismissLiesBottomSheet() {
        _state.update { state ->
            state.copy(
                liedByUsers = emptyList(),
                showLiesBottomSheet = false,
                selectedPostLies = 0
            )
        }
    }

    private fun showLiesBottomSheet(
        likedByUserId: List<String>
    ) {
        _state.update {
            it.copy(
                isLiedByUsersLoad = true,
                showLiesBottomSheet = true
            )
        }
        viewModelScope.launch {
            getLiedByUsersUseCase(
                likedByUserId
            ).fold(
                onSuccess = { users ->
                    _state.update { state ->
                        state.copy(
                            isLiedByUsersLoad = false,
                            liedByUsers = users,
                            selectedPostLies = users.size
                        )
                    }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLiedByUsersLoad = false
                        )
                    }
                }
            )
        }
    }
}