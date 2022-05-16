package com.justone.ui.offline

import androidx.activity.compose.BackHandler
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.justone.foundation.network.ResourceState
import com.justone.ui.widgets.CountDownTimer
import com.justone.ui.widgets.DialogContainer
import kotlinx.coroutines.flow.collectLatest

val LocalJustOneActor = compositionLocalOf<(OfflineAction) -> Unit> {
    error("on JustOneActor provided")
}

@Composable
fun OfflineScreen(
    navHostController: NavHostController,
    viewModel: JustOneOfflineViewModel = hiltViewModel()
) {
    val state by viewModel.flow.collectAsState()
    val actor = viewModel::dispatch

    val (dialogState, setDialogState) = remember { mutableStateOf(DialogState.HIDE) }
    val scaffoldState = rememberScaffoldState()
    var clickedWord by remember { mutableStateOf("") }

    UiEffects(viewModel, setDialogState, scaffoldState)

    val onGenerateClick = {
        if (state.words != ResourceState.Loading) actor(OfflineAction.GenerateWords)
    }
    val onWordClick = { word: String ->
        setDialogState(DialogState.WORD)
        actor(OfflineAction.TranslateWord(word))
        clickedWord = word
    }
    val onClose = { setDialogState(DialogState.HIDE) }
    val onConfirm = {
        setDialogState(DialogState.CLUE)
        actor(OfflineAction.SelectKeyword(clickedWord))
    }
    val onCountDownFinished = {
        setDialogState(DialogState.GUESS)
        actor(OfflineAction.DeduplicateClue)
    }

    CompositionLocalProvider(LocalJustOneActor provides actor) {
        WordsScreen(state, scaffoldState, onGenerateClick, onWordClick)

        if (dialogState != DialogState.HIDE) {
            DialogContainer(onClose) { dialogWidth ->
                when (dialogState) {
                    DialogState.HIDE -> {}
                    DialogState.WORD -> WordShowing(dialogWidth, clickedWord, state.translation, onClose, onConfirm)
                    DialogState.CLUE -> {
                        // no need to trigger the recomposition of CountDownTimer when state.submittedClues changed
                        CountDownTimer(state.clueTimer, dialogWidth, onCountDownFinished)
                        CluePreparing(state.playersNumber, state.submittedClues)
                    }
                    DialogState.GUESS -> ClueShowing(dialogWidth, state.submittedClues, setDialogState)
                }
            }
        }
    }

    BackHandler {
        navHostController.popBackStack()
    }
}


@Composable
private fun UiEffects(
    viewModel: JustOneOfflineViewModel,
    setDialogState: (DialogState) -> Unit,
    scaffoldState: ScaffoldState
) {
    val actor = viewModel::dispatch
    LaunchedEffect(Unit) {
        viewModel.events.collectLatest { event ->
            when (event) {
                OfflineEvent.ClueComplete -> {
                    setDialogState(DialogState.GUESS)
                    actor(OfflineAction.DeduplicateClue)
                }
                OfflineEvent.InvalidPlayerNumber -> {
                    scaffoldState.snackbarHostState.showSnackbar("Please add at least 2 player")
                }
            }
        }
    }
}
