package com.example.justone

import androidx.lifecycle.viewModelScope
import com.example.justone.data.WordGeneratorRepository
import com.example.justone.model.WordGeneratorAction
import com.example.justone.model.WordGeneratorState
import com.example.justone.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WordGeneratorViewModel @Inject constructor(
    private val repository: WordGeneratorRepository
) : BaseViewModel<WordGeneratorState, WordGeneratorAction>() {
    private val wordsNumber: Int = 5

    override fun configureInitState(): WordGeneratorState {
        return WordGeneratorState(words = emptyList(), wordsNumber = wordsNumber)
    }

    override fun dispatch(action: WordGeneratorAction) {
        when (action) {
            WordGeneratorAction.GenerateWords -> getRandomWords()
        }
    }

    private fun getRandomWords() {
        updateState { copy(words = emptyList(), isLoading = true) }
        viewModelScope.launch {
            repository.getRandomWordList(wordsNumber)
                .onSuccess { wordList ->
                    updateState { copy(words = wordList) }
                }
            updateState { copy(isLoading = false) }
        }
    }
}
