package com.rk.pace.presentation.screens.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.domain.use_case.feed.GetFeedUseCase
import com.rk.pace.domain.use_case.run.GetARunsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val getRunsUseCase: GetARunsUseCase,
    private val getFeedUseCase: GetFeedUseCase
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
}