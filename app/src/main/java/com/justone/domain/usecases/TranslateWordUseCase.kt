package com.justone.domain.usecases

import com.justone.data.JustOneWordsRepository
import com.justone.foundation.useCase.BaseCoUseCase
import javax.inject.Inject

class TranslateWordUseCase @Inject constructor(
    private val repository: JustOneWordsRepository
) : BaseCoUseCase<String, String>() {

    override suspend fun execute(parameters: String): String {
        return repository.translateWord(parameters)
    }
}
