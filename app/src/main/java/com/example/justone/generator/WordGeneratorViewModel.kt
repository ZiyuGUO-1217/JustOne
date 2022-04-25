package com.example.justone.generator

import androidx.lifecycle.viewModelScope
import com.example.foundation.BaseViewModel
import com.example.foundation.network.onSuccess
import com.example.justone.generator.data.WordGeneratorRepository
import com.example.justone.generator.model.WordGeneratorAction
import com.example.justone.generator.model.WordGeneratorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class WordGeneratorViewModel @Inject constructor(
    private val repository: WordGeneratorRepository
) : BaseViewModel<WordGeneratorState, WordGeneratorAction>() {
    private val wordsNumber: Int = 5
    private val timer: Int = 60

    override fun configureInitState(): WordGeneratorState {
        return WordGeneratorState(words = emptyList(), wordsNumber = wordsNumber, timer = timer)
    }

    override fun dispatch(action: WordGeneratorAction) {
        when (action) {
            WordGeneratorAction.GenerateWords -> getRandomWords()
        }
    }

    private fun getRandomWords() {
        updateState { copy(words = emptyList(), isLoading = true) }
        viewModelScope.launch {
            delay(3000L)
            repository.getRandomWordList(wordsNumber)
                .onSuccess { wordList ->
                    updateState { copy(words = wordList) }
                }
            updateState { copy(isLoading = false) }
        }
    }
}
