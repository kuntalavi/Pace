package com.rk.pace.presentation.screens.feed

sealed interface FeedAction {

    data object Refresh : FeedAction

    data object OnAddClick: FeedAction

    data class OnRunClick(
        val runId: String
    ) : FeedAction

    data class OnUserClick(
        val userId: String
    ) : FeedAction

}