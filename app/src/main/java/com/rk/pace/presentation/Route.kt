package com.rk.pace.presentation

import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    sealed interface Root : Route {

        @Serializable
        data object Top : Root

        @Serializable
        data object Run : Root {
            val runUriPattern = "https://pace.rk.com/${this}"
            val deepLinks = listOf(
                navDeepLink {
                    uriPattern = runUriPattern
                }
            )
        }

    }

    sealed interface Top : Route {
        @Serializable
        data object Home : Top

        @Serializable
        data object You : Top
    }
}