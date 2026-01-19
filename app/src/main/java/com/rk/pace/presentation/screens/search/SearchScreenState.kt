package com.rk.pace.presentation.screens.search

import com.rk.pace.domain.model.User

data class SearchScreenState(
    val query: String = "",
    val isSearching: Boolean = false,
    val results: List<User> = emptyList()
)