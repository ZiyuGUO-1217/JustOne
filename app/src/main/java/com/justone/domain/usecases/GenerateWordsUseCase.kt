package com.justone.domain.usecases

import com.justone.data.JustOneWordsRepository
import com.justone.foundation.useCase.BaseCoUseCase
import javax.inject.Inject

class GenerateWordsUseCase @Inject constructor(
    private val repository: JustOneWordsRepository
) : BaseCoUseCase<Unit, List<String>>() {
    companion object {
        const val WORDS_NUMBER: Int = 5
    }

    override suspend fun execute(parameters: Unit): List<String> {
        return repository.getRandomWordList(WORDS_NUMBER)
    }
}
