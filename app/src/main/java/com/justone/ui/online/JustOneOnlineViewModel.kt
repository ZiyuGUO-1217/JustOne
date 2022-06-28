package com.justone.ui.online

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.justone.domain.JustOneOnlineAdapter
import com.justone.domain.usecases.TimerUseCase
import com.justone.domain.usecases.CheckPlayerNumberUseCase
import com.justone.domain.usecases.DeduplicateClueUseCase
import com.justone.domain.usecases.GenerateWordsUseCase
import com.justone.domain.usecases.TranslateWordUseCase
import com.justone.foundation.BaseViewModel
import com.justone.foundation.network.ResourceState
import com.justone.foundation.network.onError
import com.justone.foundation.network.onSuccess
import com.justone.ui.JustOneScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

sealed interface OnlineEvent {
    object InvalidPlayerNumber: OnlineEvent
}

@HiltViewModel
class JustOneOnlineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val generateWords: GenerateWordsUseCase,
    private val translateWord: TranslateWordUseCase,
    private val deduplicateClue: DeduplicateClueUseCase,
    private val checkPlayerNumber: CheckPlayerNumberUseCase,
    private val onlineAdapter: JustOneOnlineAdapter
) : BaseViewModel<JustOneOnlineState, OnlineAction, OnlineEvent>() {

    override fun configureInitState(): JustOneOnlineState {
        val clueTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Online.KEY_CLUE_TIMER)
        val guessTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Online.KEY_GUESS_TIMER)
        return JustOneOnlineState(
            wordsNumber = GenerateWordsUseCase.WORDS_NUMBER,
            clueTimer = clueTimer ?: TimerUseCase.DEFAULT_CLUE_TIMER,
            guessTimer = guessTimer ?: TimerUseCase.DEFAULT_GUESS_TIMER
        )
    }

    override fun dispatch(action: OnlineAction) {
        when (action) {
            is OnlineAction.SetupOnlineGame -> setupOnlineGame(action.roomId, action.playerId)
            OnlineAction.GenerateWords -> {
                if (checkPlayerNumber(state.playerList.size)) {
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
        updateState { copy(words = ResourceState.Loading) }
        viewModelScope.launch {
            generateWords(Unit)
                .onSuccess { words ->
                    updateState { copy(words = ResourceState.Success(words)) }
                }
                .onError { error ->
                    updateState { copy(words = ResourceState.Error(error)) }
                }
        }
    }

    private fun translateWord(word: String) {
        updateState { copy(translation = ResourceState.Loading) }
        viewModelScope.launch {
            translateWord(parameters = word)
                .onSuccess { translation ->
                    updateState { copy(translation = ResourceState.Success(translation)) }
                }
                .onError { error ->
                    updateState { copy(translation = ResourceState.Error(error)) }
                }
        }
    }

    private fun deduplicateClue() {
        val clues = state.collectedClues
        val validClues = deduplicateClue(clues, state.keyword)
        updateState { copy(collectedClues = validClues) }
    }

    private fun submitClue(clue: String) {
    }
}
