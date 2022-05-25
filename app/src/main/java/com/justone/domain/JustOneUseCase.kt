package com.justone.domain

import com.justone.data.JustOneRepository
import com.justone.domain.utils.ClueUtils
import com.justone.foundation.network.ResourceState
import com.justone.foundation.network.onError
import com.justone.foundation.network.onSuccess
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class JustOneUseCase @Inject constructor(
    private val repository: JustOneRepository
) {
    companion object {
        const val DEFAULT_CLUE_TIMER: Int = 120
        const val DEFAULT_GUESS_TIMER: Int = 90
        const val WORDS_NUMBER: Int = 5
        const val MINIMUM_PLAYER_NUMBER: Int = 2
    }

    private val _wordList = MutableSharedFlow<ResourceState<List<String>>>()
    val wordList = _wordList.asSharedFlow()

    private val _translation = MutableSharedFlow<ResourceState<String>>()
    val translation = _translation.asSharedFlow()

    suspend fun getRandomWords() {
        _wordList.emit(ResourceState.Loading)
        repository.getRandomWordList(WORDS_NUMBER)
            .onSuccess { wordList ->
                _wordList.emit(ResourceState.Success(wordList))
            }
            .onError { errorType ->
                _wordList.emit(ResourceState.Error(errorType))
            }
    }

    suspend fun translateWord(word: String) {
        _translation.emit(ResourceState.Loading)
        repository.translateWord(word.lowercase())
            .onSuccess { result ->
                _translation.emit(ResourceState.Success(result))
            }
            .onError { errorType ->
                _translation.emit(ResourceState.Error(errorType))
            }
    }

    fun deduplicateClue(clues: List<String>, keyword: String): List<String> {
        val validClues = ClueUtils.checkClueValidation(clues, keyword)
        return ClueUtils.deduplicateClues(validClues)
    }

    fun isValidPlayerNumber(playerNumber: Int) = playerNumber >= MINIMUM_PLAYER_NUMBER

    fun checkAnswer(answer: String, guess: String) = ClueUtils.isSameClue(answer, guess)
}
