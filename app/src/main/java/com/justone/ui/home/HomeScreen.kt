package com.justone.ui.home

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
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
    val scaffoldState = rememberScaffoldState()

    UiEffects(viewModel, navHostController, scaffoldState)

    val clueTimer = remember{ mutableStateOf(state.clueTimer)}
    val guessTimer = remember{ mutableStateOf(state.guessTimer)}

    Scaffold(modifier = Modifier.fillMaxSize(), scaffoldState = scaffoldState) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val isValidClueTimer = isValidTimer(clueTimer.value)
            val isValidGuessTimer = isValidTimer(guessTimer.value)
            val isStartButtonsEnable = isValidClueTimer && isValidGuessTimer
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimerInputField(clueTimer,isValidClueTimer, guessTimer, isValidGuessTimer, actor)
                Spacer(modifier = Modifier.height(48.dp))
                GameStartButtons(isStartButtonsEnable, actor)
            }
        }
    }
}

@Composable
private fun TimerInputField(
    clueTimer: MutableState<String>,
    isValidClueTimer: Boolean,
    guessTimer: MutableState<String>,
    isValidGuessTimer: Boolean,
    actor: (action: HomeAction) -> Unit
) {
    TextInputFieldWithErrorMessage(
        inputValue = clueTimer.value,
        label = "Time for clue",
        isError = isValidClueTimer.not(),
        errorMessage = "Must be DigitsOnly"
    ) { timer ->
        clueTimer.value = timer
        if (isValidTimer(timer)) actor(HomeAction.UpdateClueTimer(timer))
    }
    Spacer(modifier = Modifier.height(32.dp))
    TextInputFieldWithErrorMessage(
        inputValue = guessTimer.value,
        label = "Time for guess",
        isError = isValidGuessTimer.not(),
        errorMessage = "Must be DigitsOnly"
    ) { timer ->
        guessTimer.value = timer
        if (isValidTimer(timer)) actor(HomeAction.UpdateGuessTimer(timer))
    }
}

@Composable
private fun GameStartButtons(
    isEnable: Boolean,
    actor: (HomeAction) -> Unit
) {
    Row(
        modifier = Modifier.wrapContentSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledButton(text = "Online", isEnable = isEnable) {
            actor(HomeAction.NavigateToOnlineScreen)
        }
        Spacer(modifier = Modifier.width(32.dp))
        FilledButton(text = "Offline", isEnable = isEnable) {
            actor(HomeAction.NavigateToOfflineScreen)
        }
    }
}

private fun isValidTimer(timer: String) = with(timer.trim()) {
    isDigitsOnly() && isNotBlank()
}

@Composable
private fun UiEffects(viewModel: HomeViewModel, navHostController: NavHostController, scaffoldState: ScaffoldState) {
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
                    scaffoldState.snackbarHostState.showSnackbar("Input value must bigger than 0")
                }
            }
        }
    }
}
