package com.justone.ui

import androidx.navigation.NavHostController
import com.justone.foundation.navigation.ScreenRoute

sealed interface JustOneScreenRoute : ScreenRoute {
    object Setup : JustOneScreenRoute {
        override val route: String = "Setup"
    }

    object Offline : JustOneScreenRoute {
        const val KEY_CLUE_TIMER = "clueTimer"
        const val KEY_GUESS_TIMER = "guessTimer"

        override val route: String = "Offline/{$KEY_CLUE_TIMER}/{$KEY_GUESS_TIMER}"

        fun navigate(navHostController: NavHostController, clueTimer: Int, guessTimer: Int) {
            navigate(
                navHostController = navHostController,
                arguments = mapOf(
                    KEY_CLUE_TIMER to clueTimer,
                    KEY_GUESS_TIMER to guessTimer
                )
            )
        }
    }

    object Online : JustOneScreenRoute {
        const val KEY_CLUE_TIMER = "clueTimer"
        const val KEY_GUESS_TIMER = "guessTimer"

        override val route: String = "Online/{${KEY_CLUE_TIMER}}/{${KEY_GUESS_TIMER}}"

        fun navigate(navHostController: NavHostController, clueTimer: Int, guessTimer: Int) {
            navigate(
                navHostController = navHostController,
                arguments = mapOf(
                    KEY_CLUE_TIMER to clueTimer,
                    KEY_GUESS_TIMER to guessTimer
                )
            )
        }
    }
}
