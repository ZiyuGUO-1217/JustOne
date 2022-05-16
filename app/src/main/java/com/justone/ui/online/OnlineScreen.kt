package com.justone.ui.online

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

val LocalOnlineActor = compositionLocalOf<(OnlineAction) -> Unit> {
    error("on JustOneActor provided")
}

@Composable
fun OnlineScreen(
    navHostController: NavHostController,
    viewModel: JustOneOnlineViewModel = hiltViewModel()
) {
    val state by viewModel.flow.collectAsState()

    CompositionLocalProvider(LocalOnlineActor provides viewModel::dispatch) {
        Scaffold(modifier = Modifier.fillMaxSize()) {}
        if (state.isOnlineSetupFinished.not()) SetupDialog { navHostController.popBackStack() }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}
