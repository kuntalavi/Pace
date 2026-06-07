package com.rk.pace.presentation.screens.feed

import com.rk.pace.domain.model.FeedPost

sealed interface FeedAction {

    data object Refresh : FeedAction

    data class OnPostLieIconClick(
        val post: FeedPost
    ) : FeedAction

    data class OnPostLiesClick(
        val post: FeedPost
    ) : FeedAction

    data object DismissLiesBottomSheet : FeedAction

    data class OnRunClick(
        val userId: String,
        val runId: String
    ) : FeedAction

    data class OnUserClick(
        val userId: String
    ) : FeedAction

}