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
): BaseViewModel<WordGeneratorState, WordGeneratorAction>() {

    init {
        getRandomWords()
    }

    override fun configureInitState(): WordGeneratorState {
        return WordGeneratorState(words = emptyList())
    }

    override fun dispatch(action: WordGeneratorAction) {
        when (action) { }
    }

    private fun getRandomWords() {
        viewModelScope.launch {
            repository.getRandomWordList()
                .onSuccess { wordList ->
                    updateState { copy(words = wordList) }
                }
        }
    }
}