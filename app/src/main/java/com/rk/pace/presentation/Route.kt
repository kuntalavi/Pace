package com.rk.pace.presentation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    sealed interface Root : Route {

        @Serializable
        data object Top : Root

        @Serializable
        data object Run : Root

    }

    sealed interface Top : Route {
        @Serializable
        data object Home : Top

        @Serializable
        data object You : Top
    }
}