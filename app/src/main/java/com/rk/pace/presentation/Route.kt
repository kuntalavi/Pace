package com.rk.pace.presentation

import androidx.navigation.navDeepLink
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {

    sealed interface Root : Route {

        @Serializable
        data object Auth : Root

        @Serializable
        data object BotNav : Root

        @Serializable
        data object ActiveRun : Root

        @Serializable
        data object Search: Root

        @Serializable
        data class UserProfile(
            val userId: String
        ): Root

        @Serializable
        data class RunStats(
            val runId: String
        ) : Route

    }

    sealed interface Auth : Route {
        @Serializable
        data object Welcome: Auth

        @Serializable
        data object SignIn : Auth

        @Serializable
        data object SignUp : Auth
    }

    sealed interface BotNav : Route {
        @Serializable
        data object Feed : BotNav

        @Serializable
        data object Stats: BotNav

        @Serializable
        data object MyProfile : BotNav
    }

    sealed interface ActiveRun : Route {
        @Serializable
        data object Run : ActiveRun {
            val uri = "https://pace.rk.com/${this}"
            val deepL = listOf(
                navDeepLink {
                    uriPattern = uri
                }
            )
        }

        @Serializable
        data object SaveRun : ActiveRun
    }

}


//        @Serializable
//        data object Run : Route {
//            val uri = "https://pace.rk.com/${this}"
//            val deepL = listOf(
//                navDeepLink {
//                    uriPattern = uri
//                }
//            )
//        }
//
//        @Serializable
//        data object SaveRun : Route

//sealed interface Root : Route {
//
//    @Serializable
//    data object Auth : Root
//
//    @Serializable
//    data object Top : Root
//}

/*
bottom nav screens => home , user and in middle a large fab for start run screen
full screens => run screen and run stats screen and auth screens
 */


//@Serializable
//data object Top : Root

//sealed interface Top : Route {
//    @Serializable
//    data object Runs : Top
//
//    @Serializable
//    data object Summary : Top
//}