package com.justone.ui.offline

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.justone.domain.usecases.CheckAnswerUseCase
import com.justone.domain.usecases.CheckPlayerNumberUseCase
import com.justone.domain.usecases.DeduplicateClueUseCase
import com.justone.domain.usecases.GenerateWordsUseCase
import com.justone.domain.usecases.TimerUseCase
import com.justone.domain.usecases.TranslateWordUseCase
import com.justone.foundation.BaseViewModel
import com.justone.foundation.network.ResourceState
import com.justone.foundation.network.onError
import com.justone.foundation.network.onSuccess
import com.justone.ui.JustOneScreenRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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
    private val generateWords: GenerateWordsUseCase,
    private val translateWord: TranslateWordUseCase,
    private val deduplicateClue: DeduplicateClueUseCase,
    private val checkPlayerNumber: CheckPlayerNumberUseCase,
    private val checkAnswer: CheckAnswerUseCase,
) : BaseViewModel<JustOneOfflineState, OfflineAction, OfflineEvent>() {

    override fun configureInitState(): JustOneOfflineState {
        val clueTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_CLUE_TIMER)
        val guessTimer = savedStateHandle.get<Int>(JustOneScreenRoute.Offline.KEY_GUESS_TIMER)
        return JustOneOfflineState(
            wordsNumber = GenerateWordsUseCase.WORDS_NUMBER,
            clueTimer = clueTimer ?: TimerUseCase.DEFAULT_CLUE_TIMER,
            guessTimer = guessTimer ?: TimerUseCase.DEFAULT_GUESS_TIMER
        )
    }

    override fun dispatch(action: OfflineAction) {
        when (action) {
            OfflineAction.GenerateWords -> {
                setupGame()
                if (checkPlayerNumber(state.playersNumber)) {
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
        val validClues = deduplicateClue(state.submittedClues, state.keyword)
        updateState {
            copy(submittedClues = validClues)
        }
    }

    private fun addPlayer() {
        val currentPlayerNumber = state.playersNumber
        updateState { copy(playersNumber = currentPlayerNumber + 1) }
    }

    private fun removePlayer() {
        val updatePlayerNumber = state.playersNumber - 1
        if (checkPlayerNumber(updatePlayerNumber)) {
            updateState { copy(playersNumber = updatePlayerNumber) }
        }
    }

    private fun checkAnswer(guess: String) {
        val isAnswerCorrect = checkAnswer(state.keyword, guess)

        updateState { copy(isAnswerCorrect = isAnswerCorrect) }
        if (isAnswerCorrect) {
            sendEvent(OfflineEvent.CorrectAnswer)
        } else {
            sendEvent(OfflineEvent.WrongAnswer)
        }
    }
}
