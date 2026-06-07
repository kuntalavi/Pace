package com.rk.pace.presentation.screens.my_profile

interface MyProfileEvent {

    data object OnEditClick : MyProfileEvent

    data class OnFollowersClick(
        val userId: String,
        val tab: Int
    ) : MyProfileEvent

    data class OnFollowingClick(
        val userId: String,
        val tab: Int
    ) : MyProfileEvent

    data class OnRunClick(
        val userId: String,
        val runId: String
    ): MyProfileEvent

    data object ChangesSaved : MyProfileEvent
    data class ChangesSaveError(
        val message: String
    ) : MyProfileEvent

    data object OnBack : MyProfileEvent

}