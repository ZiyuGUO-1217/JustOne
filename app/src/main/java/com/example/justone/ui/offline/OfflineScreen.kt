package com.example.justone.ui.offline

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.justone.model.JustOneAction
import com.example.justone.model.JustOneViewModel

val LocalJustOneActor = compositionLocalOf<(JustOneAction) -> Unit> {
    error("on JustOneActor provided")
}

@Composable
fun OfflineScreen(navHostController: NavHostController) {
    val viewModel: JustOneViewModel = hiltViewModel()
    val state by viewModel.flow.collectAsState()

    CompositionLocalProvider(LocalJustOneActor provides viewModel::dispatch) {
        WordsScreen(state)
    }

    BackHandler {
        navHostController.popBackStack()
    }
}
