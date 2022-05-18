package com.justone.ui.home

import com.justone.domain.JustOneUseCase
import com.justone.foundation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed interface HomeAction {
    data class UpdateClueTimer(val timer: String) : HomeAction
    data class UpdateGuessTimer(val timer: String) : HomeAction

    object NavigateToOnlineScreen : HomeAction
    object NavigateToOfflineScreen : HomeAction
}

sealed interface HomeEvent {
    data class NavigateToOnlineScreen(val clueTimer: Int, val guessTimer: Int) : HomeEvent
    data class NavigateToOfflineScreen(val clueTimer: Int, val guessTimer: Int) : HomeEvent
    object ValidationFailed: HomeEvent
}

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel<HomeState, HomeAction, HomeEvent>() {

    override fun configureInitState(): HomeState {
        return HomeState(
            clueTimer = JustOneUseCase.DEFAULT_CLUE_TIMER.toString(),
            guessTimer = JustOneUseCase.DEFAULT_GUESS_TIMER.toString()
        )
    }

    override fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.UpdateClueTimer -> updateClueTimer(action.timer)
            is HomeAction.UpdateGuessTimer -> updateGuessTimer(action.timer)
            HomeAction.NavigateToOfflineScreen -> navigateToOfflineScreen()
            HomeAction.NavigateToOnlineScreen -> navigateToOnlineScreen()
        }
    }

    private fun updateClueTimer(timer: String) {
        updateState { copy(clueTimer = timer) }
    }

    private fun updateGuessTimer(timer: String) {
        updateState { copy(guessTimer = timer) }
    }

    private fun navigateToOnlineScreen() {
        val onValidationFail = { sendEvent(HomeEvent.ValidationFailed) }
        checkTimerValidation(onValidationFail) { clueTimer: Int, guessTimer: Int ->
            sendEvent(HomeEvent.NavigateToOnlineScreen(clueTimer, guessTimer))
        }
    }

    private fun navigateToOfflineScreen() {
        val onValidationFail = { sendEvent(HomeEvent.ValidationFailed) }
        checkTimerValidation(onValidationFail) { clueTimer: Int, guessTimer: Int ->
            sendEvent(HomeEvent.NavigateToOfflineScreen(clueTimer, guessTimer))
        }
    }

    private fun checkTimerValidation(onValidationFail: () -> Unit, onValidationSuccess: (Int, Int) -> Unit) {
        val clueTimer = state.clueTimer.trim().toInt()
        val guessTimer = state.guessTimer.trim().toInt()
        if (clueTimer > 0 && guessTimer > 0) {
            onValidationSuccess(clueTimer, guessTimer)
        } else {
            onValidationFail()
        }
    }
}
