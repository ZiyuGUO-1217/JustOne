package com.justone.model.online

import androidx.lifecycle.viewModelScope
import com.justone.foundation.BaseViewModel
import com.justone.domain.JustOneOnlineAdapter
import com.justone.domain.JustOneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class JustOneOnlineViewModel @Inject constructor(
    private val useCase: JustOneUseCase,
    private val onlineAdapter: JustOneOnlineAdapter,
) : BaseViewModel<JustOneOnlineState, OnlineAction>() {

    init {
        viewModelScope.launch {
            useCase.wordList.collect { wordList ->
                updateState { copy(words = wordList) }
            }
        }
        viewModelScope.launch {
            useCase.translation.collect { translation ->
                updateState { copy(translation = translation) }
            }
        }
    }

    override fun configureInitState(): JustOneOnlineState {
        return JustOneOnlineState(timer = useCase.countDownTimer)
    }

    override fun dispatch(action: OnlineAction) {
        when (action) {
            is OnlineAction.SetupOnlineGame -> setupOnlineGame(action.roomId, action.playerId)
            OnlineAction.GenerateWords -> getRandomWords()
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
            useCase.getRandomWords(useCase.wordsNumber)
        }
    }

    private fun translateWord(word: String) {
        viewModelScope.launch {
            useCase.translateWord(word)
        }
    }

    private fun deduplicateClue() {
        val clues = state.collectedClues
        updateState { copy(collectedClues = useCase.deduplicateClue(clues)) }
    }

    private fun submitClue(clue: String) {
    }
}
