package com.justone.ui.offline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.justone.domain.JustOneUseCase
import com.justone.foundation.BaseViewModel
import com.justone.foundation.network.ResourceState
import com.justone.ui.JustOneScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface OfflineEvent {
    object ClueComplete : OfflineEvent
    object InvalidPlayerNumber : OfflineEvent
    object CorrectAnswer : OfflineEvent
    object WrongAnswer : OfflineEvent
}

@HiltViewModel
class JustOneOfflineViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val useCase: JustOneUseCase
) : BaseViewModel<JustOneOfflineState, OfflineAction, OfflineEvent>() {

    init {
        // todo use this method or let useCase send a flow
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

    override fun configureInitState(): JustOneOfflineState {
        val clueTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_CLUE_TIMER)
        val guessTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_GUESS_TIMER)
        return JustOneOfflineState(
            wordsNumber = JustOneUseCase.WORDS_NUMBER,
            clueTimer = clueTimer ?: JustOneUseCase.DEFAULT_CLUE_TIMER,
            guessTimer = guessTimer ?: JustOneUseCase.DEFAULT_GUESS_TIMER
        )
    }

    override fun dispatch(action: OfflineAction) {
        when (action) {
            OfflineAction.GenerateWords -> {
                setupGame()
                if (useCase.isValidPlayerNumber(state.playersNumber)) {
                    getRandomWords()
                } else {
                    sendEvent(OfflineEvent.InvalidPlayerNumber)
                }
            }
            is OfflineAction.TranslateWord -> translateWord(action.word)
            is OfflineAction.SelectKeyword -> onKeywordSelected(action.keyword)
            is OfflineAction.SubmitClue -> submitClue(action.clue)
            OfflineAction.DeduplicateClue -> deduplicateClue()
            OfflineAction.AddPlayer -> addPlayer()
            OfflineAction.RemovePlayer -> removePlayer()
            is OfflineAction.CheckAnswer -> checkAnswer(action.guess)
        }
    }

    private fun setupGame() {
        updateState {
            copy(
                keyword = "",
                submittedClues = emptyList(),
                isAnswerCorrect = false
            )
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

    private fun onKeywordSelected(keyword: String) {
        updateState {
            copy(
                words = ResourceState.Empty,
                keyword = keyword
            )
        }
    }

    private fun submitClue(inputClue: String) {
        val updatedClues = state.submittedClues.toMutableList()
            .apply { add(inputClue) }
        updateState { copy(submittedClues = updatedClues) }

        if (updatedClues.size == state.playersNumber - 1) sendEvent(OfflineEvent.ClueComplete)
    }

    private fun deduplicateClue() {
        updateState {
            copy(submittedClues = useCase.deduplicateClue(state.submittedClues, state.keyword))
        }
    }

    private fun addPlayer() {
        val currentPlayerNumber = state.playersNumber
        updateState { copy(playersNumber = currentPlayerNumber + 1) }
    }

    private fun removePlayer() {
        val updatePlayerNumber = state.playersNumber - 1
        if (useCase.isValidPlayerNumber(updatePlayerNumber)) {
            updateState { copy(playersNumber = updatePlayerNumber) }
        }
    }

    private fun checkAnswer(guess: String) {
        val isAnswerCorrect = useCase.checkAnswer(state.keyword, guess)

        updateState { copy(isAnswerCorrect = isAnswerCorrect) }
        if (isAnswerCorrect) {
            sendEvent(OfflineEvent.CorrectAnswer)
        } else {
            sendEvent(OfflineEvent.WrongAnswer)
        }
    }
}
