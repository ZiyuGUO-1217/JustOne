package com.justone.foundation.navigation

import android.net.Uri
import androidx.navigation.NavArgument
import androidx.navigation.NavHostController

interface ScreenRoute {
    val route: String

    fun navigate(navHostController: NavHostController) {
        navHostController.navigate(route)
    }

    fun navigate(navHostController: NavHostController, argument: NavArgument) {}

    fun navigate(navHostController: NavHostController, arguments: Map<String, Any>) {
        val routeWithArguments = arguments.keys.fold(route) { route, key ->
            route.replace("{$key}", Uri.encode(arguments[key].toString()))
        }
        navHostController.navigate(routeWithArguments)
    }
}
