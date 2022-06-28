package com.justone.data

import com.justone.data.model.WordTranslatorRequest
import com.justone.data.remoteDataSource.GeneratorService
import com.justone.data.remoteDataSource.TranslatorService
import com.justone.domain.repositories.WordsRepository
import javax.inject.Inject

class JustOneWordsRepository @Inject constructor(
    private val generatorService: GeneratorService,
    private val translatorService: TranslatorService
): WordsRepository {
    override suspend fun getRandomWordList(wordsNumber: Int): List<String> {
        val wordsResponse = generatorService.getRandomWords(
            table = "pictionary",
            language = "en",
            limit = wordsNumber,
            random = "yes"
        )
        return wordsResponse.data
    }

    override suspend fun translateWord(word: String): String {
        val request = WordTranslatorRequest(
            word = word,
            translationType = ENGLISH_TO_CHINESE,
        )
        val response = translatorService.translateWord(request)
        return response.target
    }

    companion object {
        const val ENGLISH_TO_CHINESE = "en2zh"
    }
}
