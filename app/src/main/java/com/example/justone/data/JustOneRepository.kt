package com.example.justone.data

import com.example.foundation.network.ApiResult
import com.example.foundation.network.callApi
import javax.inject.Inject

class JustOneRepository @Inject constructor(
    private val generatorService: GeneratorService,
    private val translatorService: TranslatorService
) {
    suspend fun getRandomWordList(wordsNumber: Int): ApiResult<List<String>> = callApi {
        val wordsResponse = generatorService.getRandomWords(
            table = "pictionary",
            language = "en",
            limit = wordsNumber,
            random = "yes"
        )
        return ApiResult.Success(wordsResponse.data)
    }

    suspend fun translateWord(word: String): ApiResult<String> = callApi {
        val request = WordTranslatorRequest(
            word = word,
            translationType = ENGLISH_TO_CHINESE,
        )
        val response = translatorService.translateWord(request)
        return ApiResult.Success(response.target)
    }

    companion object {
        const val ENGLISH_TO_CHINESE = "en2zh"
    }
}
