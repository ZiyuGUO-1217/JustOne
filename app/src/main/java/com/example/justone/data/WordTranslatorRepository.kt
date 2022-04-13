package com.example.justone.data

import com.example.justone.network.ApiResult
import com.example.justone.network.callApi
import javax.inject.Inject

class WordTranslatorRepository @Inject constructor(
    private val translatorService: TranslatorService
) {

    suspend fun translateWord(): ApiResult<String> = callApi {
        val request = WordTranslateRequest(
            word = "hello",
            translationType = "en2zh",
        )
        val response = translatorService.translateWord(request)
        return ApiResult.Success(response)
    }
}
