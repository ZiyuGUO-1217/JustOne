package com.example.justone.translator

import androidx.lifecycle.viewModelScope
import com.example.foundation.network.onError
import com.example.foundation.network.onSuccess
import com.example.justone.translator.data.WordTranslatorRepository
import com.example.justone.translator.model.WordTranslatorAction
import com.example.justone.translator.model.WordTranslatorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class WordTranslatorViewModel @Inject constructor(
    private val repository: WordTranslatorRepository
) : com.example.foundation.BaseViewModel<WordTranslatorState, WordTranslatorAction>() {

    override fun configureInitState(): WordTranslatorState {
        return WordTranslatorState()
    }

    override fun dispatch(action: WordTranslatorAction) {
        when (action) {
            is WordTranslatorAction.TranslateWord -> translateWord(action.word)
        }
    }

    private fun translateWord(word: String) {
        viewModelScope.launch {
            repository.translateWord(word)
                .onSuccess { result ->
                    updateState { copy(translation = com.example.foundation.network.ResourceState.Success(result)) }
                }
                .onError { errorType ->
                    updateState { copy(translation = com.example.foundation.network.ResourceState.Error(errorType)) }
                }
        }
    }
}
