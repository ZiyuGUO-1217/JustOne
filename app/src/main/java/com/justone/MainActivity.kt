package com.justone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.justone.ui.HomeScreen
import com.justone.ui.JustOneScreenRoute
import com.justone.ui.offline.OfflineScreen
import com.justone.ui.online.OnlineScreen
import com.justone.ui.theme.JustOneTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JustOneTheme {
                Content()
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    private fun Content() {
        val navHostController = rememberAnimatedNavController()
        val startDestination = JustOneScreenRoute.Setup.route

        AnimatedNavHost(navController = navHostController, startDestination = startDestination) {
            composable(route = JustOneScreenRoute.Setup.route) {
                HomeScreen(navHostController = navHostController)
            }
            composable(route = JustOneScreenRoute.Offline.route) {
                OfflineScreen(navHostController = navHostController)
            }
            composable(route = JustOneScreenRoute.Online.route) {
                OnlineScreen(navHostController = navHostController)
            }
        }
    }
}
