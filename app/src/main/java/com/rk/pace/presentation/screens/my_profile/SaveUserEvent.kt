package com.rk.pace.presentation.screens.my_profile

sealed class SaveUserEvent {
    object Success : SaveUserEvent()
    data class Error(val message: String) : SaveUserEvent()
    object Savg : SaveUserEvent()
}