package com.rk.pace.presentation.screens.my_profile

import com.rk.pace.domain.model.User

sealed interface MyProfileAction {

    data object OnEditProfileClick : MyProfileAction
    data class OnSaveChangeClick(
        val user: User
    ) : MyProfileAction

}