package com.justone.model.offline

import androidx.lifecycle.viewModelScope
import com.justone.domain.JustOneUseCase
import com.justone.foundation.BaseViewModel
import com.justone.foundation.network.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface OfflineEvent {
    object ClueComplete : OfflineEvent
    object InvalidPlayerNumber : OfflineEvent
}

@HiltViewModel
class JustOneOfflineViewModel @Inject constructor(
    private val useCase: JustOneUseCase,
) : BaseViewModel<JustOneOfflineState, OfflineAction>() {
    private val _events = MutableSharedFlow<OfflineEvent>()
    val events = _events.asSharedFlow()

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

    override fun configureInitState(): JustOneOfflineState {
        return JustOneOfflineState(
            wordsNumber = useCase.wordsNumber,
            timer = useCase.countDownTimer
        )
    }

    override fun dispatch(action: OfflineAction) {
        when (action) {
            OfflineAction.GenerateWords -> {
                cleanSubmittedClues()
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

    private fun cleanSubmittedClues() {
        updateState { copy(submittedClues = emptyList()) }
    }

    private fun sendEvent(event: OfflineEvent) {
        viewModelScope.launch {
            _events.emit(event)
        }
    }
}
