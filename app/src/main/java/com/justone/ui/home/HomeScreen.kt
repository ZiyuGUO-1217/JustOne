package com.justone.ui.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.justone.ui.JustOneScreenRoute
import com.justone.ui.widgets.FilledButton
import com.justone.ui.widgets.TextInputFieldWithErrorMessage
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(navHostController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val state by viewModel.flow.collectAsState()
    val actor = viewModel::dispatch
    val snackbarHostState = remember { SnackbarHostState() }

    UiEffects(viewModel, navHostController, snackbarHostState)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextInputFieldWithErrorMessage(
                inputValue = state.clueTimer,
                label = "Time for clue",
                isError = state.isValidClueTimer.not(),
                errorMessage = "Must be DigitsOnly"
            ) { timer ->
                actor(HomeAction.UpdateClueTimer(timer))
            }
            Spacer(modifier = Modifier.height(32.dp))
            TextInputFieldWithErrorMessage(
                inputValue = state.guessTimer,
                label = "Time for guess",
                isError = state.isValidGuessTimer.not(),
                errorMessage = "Must be DigitsOnly"
            ) { timer ->
                actor(HomeAction.UpdateGuessTimer(timer))
            }

            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier.wrapContentSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledButton(
                    text = "Online",
                    isEnable = state.isValidClueTimer && state.isValidGuessTimer
                ) {
                    actor(HomeAction.NavigateToOnlineScreen)
                }
                Spacer(modifier = Modifier.width(32.dp))
                FilledButton(
                    text = "Offline",
                    isEnable = state.isValidClueTimer && state.isValidGuessTimer
                ) {
                    actor(HomeAction.NavigateToOfflineScreen)
                }
            }
        }
    }

    SnackbarHost(hostState = snackbarHostState)
}

@Composable
fun UiEffects(viewModel: HomeViewModel, navHostController: NavHostController, snackbarHostState: SnackbarHostState) {
    LaunchedEffect(Unit) {
        viewModel.event.collectLatest { event: HomeEvent ->
            when (event) {
                is HomeEvent.NavigateToOfflineScreen -> {
                    JustOneScreenRoute.Offline.navigate(
                        navHostController = navHostController,
                        clueTimer = event.clueTimer,
                        guessTimer = event.guessTimer
                    )
                }
                is HomeEvent.NavigateToOnlineScreen -> {
                    JustOneScreenRoute.Online.navigate(
                        navHostController = navHostController,
                        clueTimer = event.clueTimer,
                        guessTimer = event.guessTimer
                    )
                }
                HomeEvent.ValidationFailed -> {
                    snackbarHostState.showSnackbar("Input value must bigger than 0")
                }
            }
        }
    }
}
