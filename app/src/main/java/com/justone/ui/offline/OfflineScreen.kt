package com.justone.ui.offline

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.justone.model.offline.OfflineAction
import com.justone.model.offline.JustOneOfflineViewModel

val LocalJustOneActor = compositionLocalOf<(OfflineAction) -> Unit> {
    error("on JustOneActor provided")
}

@Composable
fun OfflineScreen(navHostController: NavHostController) {
    val viewModel: JustOneOfflineViewModel = hiltViewModel()
    val state by viewModel.flow.collectAsState()

    CompositionLocalProvider(LocalJustOneActor provides viewModel::dispatch) {
        val wordsNumber = viewModel.useCase.wordsNumber
        WordsScreen(state, wordsNumber)
    }

    BackHandler {
        navHostController.popBackStack()
    }
}
