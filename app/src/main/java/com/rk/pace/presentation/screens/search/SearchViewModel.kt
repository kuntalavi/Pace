package com.rk.pace.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rk.pace.domain.use_case.user.SearchUsersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

    private val _state: MutableStateFlow<SearchScreenState> = MutableStateFlow(SearchScreenState())
    val state = _state

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    private fun observeSearchQuery() {
        _state.map { it.query.trim().lowercase() }
            .debounce(300L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                _state.update { it.copy(isSearching = true) }
                searchUsersUseCase(query)
            }
            .onEach { users ->
                _state.update {
                    it.copy(
                        results = users,
                        isSearching = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onQueryChange(newQuery: String) {
        _state.update {
            it.copy(query = newQuery)
        }
    }

    fun onClearQuery() {
        _state.update {
            it.copy(
                results = emptyList(),
                query = "",
                isSearching = false
            )
        }
    }
}