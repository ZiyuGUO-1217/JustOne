package com.example.justone.translator.data

import com.example.foundation.network.ApiResult
import com.example.foundation.network.callApi
import javax.inject.Inject

class WordTranslatorRepository @Inject constructor(
    private val translatorService: TranslatorService
) {

    suspend fun translateWord(word: String = "hello"): com.example.foundation.network.ApiResult<String> =
        com.example.foundation.network.callApi {
            val request = WordTranslateRequest(
                word = word,
                translationType = ENGLISH_TO_CHINESE,
            )
            val response = translatorService.translateWord(request)
            return com.example.foundation.network.ApiResult.Success(response)
        }

    companion object {
        const val ENGLISH_TO_CHINESE = "en2zh"
    }
}
