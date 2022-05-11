package com.justone.model.offline

import androidx.lifecycle.viewModelScope
import com.justone.domain.JustOneUseCase
import com.justone.foundation.BaseViewModel
import com.justone.foundation.network.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class JustOneOfflineViewModel @Inject constructor(
    private val useCase: JustOneUseCase,
) : BaseViewModel<JustOneOfflineState, OfflineAction>() {

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

    override fun configureInitState(): JustOneOfflineState {
        return JustOneOfflineState(
            timer = useCase.countDownTimer,
            wordsNumber = useCase.wordsNumber
        )
    }

    override fun dispatch(action: OfflineAction) {
        when (action) {
            OfflineAction.GenerateWords -> getRandomWords()
            is OfflineAction.TranslateWord -> translateWord(action.word)
            is OfflineAction.SelectKeyword -> onKeywordSelected(action.keyword)
            is OfflineAction.SubmitClue -> submitClue(action.clue)
            OfflineAction.DeduplicateClue -> deduplicateClue()
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
    }

    private fun deduplicateClue() {
        updateState {
            copy(submittedClues = useCase.deduplicateClue(state.submittedClues, state.keyword))
        }
    }
}
