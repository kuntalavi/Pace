package com.rk.pace.presentation.screens.feed

sealed interface FeedEvent {

    data class OnRunClick(
        val userId: String,
        val runId: String
    ) : FeedEvent

    data class OnUserClick(
        val userId: String
    ) : FeedEvent

    data class Error(
        val message: String
    ): FeedEvent

}