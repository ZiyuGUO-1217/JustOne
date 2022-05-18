package com.justone.ui.online

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.justone.domain.JustOneOnlineAdapter
import com.justone.domain.JustOneUseCase
import com.justone.foundation.BaseViewModel
import com.justone.ui.JustOneScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface OnlineEvent {
    object InvalidPlayerNumber: OnlineEvent
}

@HiltViewModel
class JustOneOnlineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: JustOneUseCase,
    private val onlineAdapter: JustOneOnlineAdapter
) : BaseViewModel<JustOneOnlineState, OnlineAction, OnlineEvent>() {

    init {
        viewModelScope.launch {
            useCase.wordList.collectLatest { wordList ->
                updateState { copy(words = wordList) }
            }
        }
        viewModelScope.launch {
            useCase.translation.collectLatest { translation ->
                updateState { copy(translation = translation) }
            }
        }
    }

    override fun configureInitState(): JustOneOnlineState {
        val clueTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Online.KEY_CLUE_TIMER)
        val guessTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Online.KEY_GUESS_TIMER)
        return JustOneOnlineState(
            wordsNumber = JustOneUseCase.WORDS_NUMBER,
            clueTimer = clueTimer ?: JustOneUseCase.DEFAULT_CLUE_TIMER,
            guessTimer = guessTimer ?: JustOneUseCase.DEFAULT_GUESS_TIMER
        )
    }

    override fun dispatch(action: OnlineAction) {
        when (action) {
            is OnlineAction.SetupOnlineGame -> setupOnlineGame(action.roomId, action.playerId)
            OnlineAction.GenerateWords -> {
                if (useCase.isValidPlayerNumber(state.playerList.size)) {
                    getRandomWords()
                } else {
                    sendEvent(OnlineEvent.InvalidPlayerNumber)
                }
            }
            is OnlineAction.SubmitClue -> submitClue(action.clue)
            is OnlineAction.TranslateWord -> translateWord(action.word)
            is OnlineAction.DeduplicateClue -> deduplicateClue()
        }
    }

    private fun setupOnlineGame(roomId: String, playerId: String) {
        if (playerId.isNotEmpty()) {
            onlineAdapter.setupGame(roomId, playerId)
            updateState { copy(isOnlineSetupFinished = true) }
        }
    }

    private fun getRandomWords() {
        viewModelScope.launch {
            useCase.getRandomWords()
        }
    }

    private fun translateWord(word: String) {
        viewModelScope.launch {
            useCase.translateWord(word)
        }
    }

    private fun deduplicateClue() {
        val clues = state.collectedClues
        updateState { copy(collectedClues = useCase.deduplicateClue(clues, keyword)) }
    }

    private fun submitClue(clue: String) {
    }
}
