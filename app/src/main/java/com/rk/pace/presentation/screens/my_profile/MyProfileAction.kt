package com.rk.pace.presentation.screens.my_profile

sealed interface MyProfileAction {

    data object OnEditClick : MyProfileAction
    data class OnFollowersClick(
        val userId: String,
        val tab: Int
    ) : MyProfileAction

    data class OnFollowingClick(
        val userId: String,
        val tab: Int
    ) : MyProfileAction

    data class OnRunClick(
        val userId: String,
        val runId: String
    ): MyProfileAction

    data object OnSaveChangesClick : MyProfileAction
    data object OnBackClick : MyProfileAction
    data object OnSignOutClick : MyProfileAction

    data class OnNameChange(
        val name: String
    ) : MyProfileAction

    data class OnUsernameChange(
        val username: String
    ) : MyProfileAction

    data class OnEmailChange(
        val email: String
    ) : MyProfileAction

    data class OnPhotoUriChange(
        val photoUri: String
    ) : MyProfileAction

}