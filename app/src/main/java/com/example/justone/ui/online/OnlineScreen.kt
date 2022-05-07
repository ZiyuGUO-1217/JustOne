package com.example.justone.ui.online

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.justone.model.JustOneViewModel
import com.example.justone.ui.offline.LocalJustOneActor

@Composable
fun OnlineScreen(navHostController: NavHostController) {
    val viewModel: JustOneViewModel = hiltViewModel()
    val state = viewModel.flow.collectAsState()

    CompositionLocalProvider(LocalJustOneActor provides viewModel::dispatch) {
        SetupDialog()
    }

    BackHandler {
        navHostController.popBackStack()
    }
}
