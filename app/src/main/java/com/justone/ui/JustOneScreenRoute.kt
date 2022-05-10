package com.justone.ui

import com.justone.foundation.navigation.ScreenRoute

sealed interface JustOneScreenRoute : ScreenRoute {
    object Setup : JustOneScreenRoute {
        override val route: String = "Setup"
    }

    object Offline : JustOneScreenRoute {
        override val route: String = "Offline"
    }

    object Online : JustOneScreenRoute {
        override val route: String = "Online"
    }
}
